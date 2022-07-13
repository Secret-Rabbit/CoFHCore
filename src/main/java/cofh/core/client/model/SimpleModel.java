package cofh.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.SimpleUnbakedGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class SimpleModel extends SimpleUnbakedGeometry<SimpleModel> {

    private final ElementsModelCoFH model;
    private final SimpleModel.IFactory<BakedModel> factory;

    public SimpleModel(ElementsModelCoFH model, SimpleModel.IFactory<BakedModel> factory) {

        this.model = model;
        this.factory = factory;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {

        return factory.create(model.bake(owner, bakery, spriteGetter, modelTransform, overrides, modelLocation));
    }

    @Override
    public void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {

        model.addQuads(owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {

        return model.getMaterials(owner, modelGetter, missingTextureErrors);
    }

    public interface IFactory<T extends BakedModel> {

        T create(BakedModel originalModel);

    }

    // region LOADER
    public static class Loader implements IGeometryLoader<SimpleModel> {

        private final SimpleModel.IFactory<BakedModel> factory;

        public Loader(SimpleModel.IFactory<BakedModel> factory) {

            this.factory = factory;
        }

        @Override
        public SimpleModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {

            return new SimpleModel(ElementsModelCoFH.Loader.INSTANCE.read(jsonObject, deserializationContext), factory);
        }

    }
    // endregion
}
