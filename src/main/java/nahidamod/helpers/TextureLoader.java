package nahidamod.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    public static Texture getTexture(final String textureString) {
        if (textures.get(textureString) == null) {
            try {
                loadTexture(textureString);
            } catch (GdxRuntimeException e) {
                return null;
            }
        }
        return textures.get(textureString);
    }

    public static TextureAtlas.AtlasRegion getPowerRegion(final String textureString) {
        Texture tex = getTexture(textureString);
        if (tex == null) {
            return null;
        }
        return new TextureAtlas.AtlasRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
    }

    private static void loadTexture(final String textureString) throws GdxRuntimeException {
        Texture texture = new Texture(textureString);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(textureString, texture);
    }
}
