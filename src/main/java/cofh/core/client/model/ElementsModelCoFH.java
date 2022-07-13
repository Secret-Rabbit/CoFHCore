package cofh.core.client.model;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.SimpleUnbakedGeometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Why does this class exist? Because Forge made some dumb decisions.
 */
public class ElementsModelCoFH extends SimpleUnbakedGeometry<ElementsModelCoFH> {

    private final List<BlockElement> elements;

    private ElementsModelCoFH(List<BlockElement> elements) {

        this.elements = elements;
    }

    @Override
    public void addQuads(IGeometryBakingContext context, IModelBuilder<?> modelBuilder, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {

        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            modelState = new SimpleModelState(modelState.getRotation().compose(rootTransform), modelState.isUvLocked());

        for (BlockElement element : elements) {
            for (Direction direction : element.faces.keySet()) {
                var face = element.faces.get(direction);
                var sprite = spriteGetter.apply(context.getMaterial(face.texture));
                var quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);

                if (face.cullForDirection == null)
                    modelBuilder.addUnculledFace(quad);
                else
                    modelBuilder.addCulledFace(modelState.getRotation().rotateTransform(face.cullForDirection), quad);
            }
        }
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {

        Set<Material> textures = Sets.newHashSet();
        if (context.hasMaterial("particle"))
            textures.add(context.getMaterial("particle"));
        for (BlockElement part : elements) {
            for (BlockElementFace face : part.faces.values()) {
                Material texture = context.getMaterial(face.texture);
                if (texture.texture().equals(MissingTextureAtlasSprite.getLocation()))
                    missingTextureErrors.add(Pair.of(face.texture, context.getModelName()));
                textures.add(texture);
            }
        }

        return textures;
    }

    public static final class Loader implements IGeometryLoader<ElementsModelCoFH> {

        public static final Loader INSTANCE = new Loader();

        private Loader() {

        }

        @Override
        public ElementsModelCoFH read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {

            if (!jsonObject.has("elements")) {
                throw new JsonParseException("An element model must have an \"elements\" member.");
            }
            List<BlockElement> elements = new ArrayList<>();
            for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject, "elements")) {
                elements.add(deserializationContext.deserialize(element, BlockElement.class));
            }
            return new ElementsModelCoFH(elements);
        }

    }

}