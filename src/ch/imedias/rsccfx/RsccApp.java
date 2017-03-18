package ch.imedias.rsccfx;

import ch.imedias.rscc.ProcessExecutor;
import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.RsccHomePresenter;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccRequestPresenter;
import ch.imedias.rsccfx.view.RsccRequestView;
import ch.imedias.rsccfx.view.RsccSupportPresenter;
import ch.imedias.rsccfx.view.RsccSupportView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsccApp extends Application {
  public static final String APP_NAME = "Remote Support";

  /**
   * Declares views for use with ViewController.
   */
  public static final String HOME_VIEW = "home";
  public static final String REQUEST_VIEW = "requestHelp";
  public static final String SUPPORT_VIEW = "supporter";

  Rscc model;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    model = new Rscc(new ProcessExecutor());
    ViewController mainView = new ViewController();

    Group root = new Group();
    root.getChildren().addAll(mainView);
    Scene scene = new Scene(root);

    // HomeView
    Node view = new RsccHomeView(model);
    ControlledPresenter presenter = new RsccHomePresenter(model, (RsccHomeView) view);
    presenter.initSize(scene);
    mainView.loadView(RsccApp.HOME_VIEW, view, presenter);

    // RequestHelpView
    view = new RsccRequestView(model);
    presenter = new RsccRequestPresenter(model, (RsccRequestView) view);
    presenter.initSize(scene);
    mainView.loadView(RsccApp.REQUEST_VIEW, view, presenter);

    // SupporterView
    view = new RsccSupportView(model);
    presenter = new RsccSupportPresenter(model, (RsccSupportView) view);
    presenter.initSize(scene);
    mainView.loadView(RsccApp.SUPPORT_VIEW, view, presenter);

    // Set initial screen
    mainView.setView(RsccApp.HOME_VIEW);

    stage.setHeight(400);
    stage.setWidth(700);
    stage.setMinWidth(250);
    stage.setMinHeight(300);
    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();

    // Initializing stylesheets
    String supporterSheet = getClass().getClassLoader()
        .getResource("css/supporterStyle.css").toExternalForm();
    String headerSheet = getClass().getClassLoader()
        .getResource("css/headerStyle.css").toExternalForm();
    String homeSheet = getClass().getClassLoader()
        .getResource("css/HomeStyle.css").toExternalForm();
    scene.getStylesheets().addAll(supporterSheet, headerSheet, homeSheet);
  }

  @Override
  public void stop() throws Exception {
    model.killConnection(model.getKey());
    super.stop();
  }
}
