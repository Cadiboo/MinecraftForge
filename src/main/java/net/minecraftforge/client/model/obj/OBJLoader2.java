/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.client.model.obj;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class OBJLoader2 implements IModelLoader<OBJModel2>
{
    public static OBJLoader2 INSTANCE = new OBJLoader2();

    private final Map<OBJModel2.ModelSettings, OBJModel2> modelCache = Maps.newHashMap();
    private final Map<ResourceLocation, MaterialLibrary2> materialCache = Maps.newHashMap();

    private IResourceManager manager = Minecraft.getInstance().getResourceManager();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        modelCache.clear();
        materialCache.clear();
        manager = resourceManager;
    }

    @Override
    public OBJModel2 read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
    {
        if (!modelContents.has("model"))
            throw new RuntimeException("OBJ Loader requires a 'model' key that points to a valid .OBJ model.");

        String modelLocation = modelContents.get("model").getAsString();

        boolean detectCullableFaces = JSONUtils.getBoolean(modelContents, "detectCullableFaces", true);
        boolean diffuseLighting = JSONUtils.getBoolean(modelContents, "diffuseLighting", false);
        boolean flipV = JSONUtils.getBoolean(modelContents, "flip-v", false);
        boolean ambientToFullbright = JSONUtils.getBoolean(modelContents, "ambientToFullbright", true);
        @Nullable
        String materialLibraryOverrideLocation = modelContents.has("materialLibraryOverride") ? JSONUtils.getString(modelContents, "materialLibraryOverride") : null;

        return loadModel(new OBJModel2.ModelSettings(new ResourceLocation(modelLocation), detectCullableFaces, diffuseLighting, flipV, ambientToFullbright, materialLibraryOverrideLocation));
    }

    public OBJModel2 loadModel(OBJModel2.ModelSettings settings)
    {
        return modelCache.computeIfAbsent(settings, (data) -> {
            IResource resource;
            try
            {
                resource = manager.getResource(settings.modelLocation);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not find OBJ model", e);
            }

            try(LineReader rdr = new LineReader(resource))
            {
                return new OBJModel2(rdr, settings);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not read OBJ model", e);
            }
        });
    }

    public MaterialLibrary2 loadMaterialLibrary(ResourceLocation materialLocation)
    {
        return materialCache.computeIfAbsent(materialLocation, (location) -> {
            IResource resource;
            try
            {
                resource = manager.getResource(location);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not find OBJ material library", e);
            }

            try(LineReader rdr = new LineReader(resource))
            {
                return new MaterialLibrary2(rdr);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not read OBJ material library", e);
            }
        });
    }
}
