package pl.polsl.informationtheory.fxml.component;

import javafx.scene.layout.Pane;

import lombok.Getter;

@Getter
public class LatexPaneView extends Pane {

    private final LatexView latexView;

    public LatexPaneView() {
        latexView = new LatexView();
        latexView.widthProperty().bind(this.widthProperty());
        latexView.heightProperty().bind(this.heightProperty());

        widthProperty().addListener(change -> latexView.update());
        heightProperty().addListener(change -> latexView.update());
        latexView.setSize(50);
        latexView.setFormula("\\overset{\\text{Lorenz system}}{\n" +
                "            \\begin{cases}\n" +
                "            \\frac { \\partial \\vec v }{\\partial t} + \\left( \\vec v \\nabla \\right) \\vec v = -\\frac {\\nabla p}{\\rho} + \\nu \\nabla ^2 \\vec v + \\vec g \\\\\n" +
                "            \\frac { \\partial \\rho }{\\partial t} + \\nabla \\cdot \\left( \\rho \\vec v \\right) = 0 \\\\\n" +
                "            \\frac { \\partial T }{\\partial t} + \\nabla \\cdot \\left( T \\vec v \\right) = \\chi \\nabla ^2 T \\\\\n" +
                "            \\rho = \\rho_0 \\left( 1 - \\gamma \\left( T - T_0 \\right) \\right)\n" +
                "            \\end{cases}\n" +
                "        }");
        getChildren().add(latexView);
    }



}
