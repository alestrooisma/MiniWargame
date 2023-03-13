package mwg.view;

import aetherdriven.view.LayeredView;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mwg.controller.BattleController;
import mwg.controller.CameraController;

public class View implements Disposable {
    // Owned
    private final Projection projection;
    private final CameraController cameraController;
    private final LayeredView view;
    private final BattleLayer battleLayer;
    private final DebugLayer debugLayer;
    private final AiLayer aiLayer;

    public View(BattleController controller) {
        // Create a projection
        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        projection = new BattleProjection(viewport);

        // Create camera controller
        cameraController = controller.createCameraController(projection);

        // Create the view
        view = new LayeredView(0.2f, 0.2f, 0.2f);
        TiledMap map = new TmxMapLoader().load("maps/default-map.tmx");
        MapLayer mapLayer = new MapLayer(camera, map);
        view.add(mapLayer);
        battleLayer = new BattleLayer(controller, projection, cameraController);
        view.add(battleLayer);
        debugLayer = new DebugLayer(battleLayer, projection, cameraController);
        view.add(debugLayer);
        aiLayer = new AiLayer(projection);
        view.add(aiLayer);
    }

    public LayeredView getView() {
        return view;
    }

    public BattleLayer getBattleLayer() {
        return battleLayer;
    }

    public DebugLayer getDebugLayer() {
        return debugLayer;
    }

    public AiLayer getAiLayer() {
        return aiLayer;
    }

    public void resize(int width, int height) {
        projection.update(width, height);
        cameraController.snap();
        view.resize(width, height);
    }

    public void render(float dt) {
        view.render(dt);
    }

    public void dispose() {
        view.dispose();
    }
}
