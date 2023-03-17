package mwg.view;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class ResourceContainer implements Disposable {
    public static ResourceContainer instance = null;
    private final ObjectMap<String, Skin> skins = new ObjectMap<>();

    private ResourceContainer() {
    }

    public void add(String id, Skin skin) {
        skins.put(id, skin);
    }

    public Skin get(String id) {
        return skins.get(id);
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<String, Skin> entry : skins) {
            Skin skin = entry.value;
            skin.dispose();
        }
        instance = null;
    }

    public static ResourceContainer create() {
        if (instance != null) {
            throw new IllegalStateException("A ResourceContainer instance already exists");
        }
        instance = new ResourceContainer();
        return instance;
    }
}
