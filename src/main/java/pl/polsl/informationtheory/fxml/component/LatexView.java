package pl.polsl.informationtheory.fxml.component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.css.*;
import javafx.css.converter.SizeConverter;
import javafx.geometry.NodeOrientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author <a href="https://github.com/egormkn">egormkn</a>
 */
@Slf4j
public class LatexView extends Canvas {

    private final SimpleObjectProperty<TeXIcon> teXiconProperty = new SimpleObjectProperty<>();
    private final StyleableFloatProperty size = new LatexStyleableFloatProperty(DEFAULT_SIZE, this);
    private final StringProperty formula = new SimpleStringProperty(DEFAULT_FORMULA);

    private static final float DEFAULT_SIZE = (float) Font.getDefault().getSize();
    private static final String DEFAULT_FORMULA = "";
    private static final String DEFAULT_STYLE_CLASS = "latex-view";

    static {
        // Load all available TTF fonts from JLatexMath packages
        BiPredicate<Path, BasicFileAttributes> isFont = (path, attr) -> path.toString().endsWith(".ttf");
        String[] packages = {
                "/org/scilab/forge/jlatexmath/fonts/",
                "/org/scilab/forge/jlatexmath/cyrillic/fonts/",
                "/org/scilab/forge/jlatexmath/greek/fonts/"
        };
        for (String pkg : packages) {
            try {
                URI uri = LatexView.class.getResource(pkg).toURI();
                Path path;
                if (uri.getScheme().equals("jar")) {
                    try {
                        path = FileSystems.newFileSystem(uri, Collections.emptyMap()).getPath(pkg);
                    } catch (FileSystemAlreadyExistsException e) {
                        path = FileSystems.getFileSystem(uri).getPath(pkg);
                    }
                } else {
                    path = Paths.get(uri);
                }
                Files.find(path, 5, isFont)
                        .map(Path::toString)
                        .map(LatexView.class::getResourceAsStream)
                        .forEach(font -> Font.loadFont(font, -1));
            } catch (URISyntaxException | IOException e) {
                log.warn("Failed to load fonts from package " + pkg);
            }
        }
    }

    /**
     * Allocates a new {@code LatexView} object.
     */
    public LatexView() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public void update() throws ParseException {
        TeXFormula teXFormula;
        try {
            teXFormula = new TeXFormula(getFormula());
        } catch (ParseException e) {
            log.warn(e.getMessage());
            teXFormula = new TeXFormula("\\textcolor{red}{\\text{Error}}");
        }

        setTexIcon(teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, getSize()));

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        FXGraphics2D graphics = new FXGraphics2D(gc);
        getTexIcon().paintIcon(null, graphics, 0, 0);
    }

    public TeXIcon getTexIcon() {
        return teXiconProperty.get();
    }

    public void setTexIcon(TeXIcon teXicon) {
        this.teXiconProperty.set(teXicon);
    }

    public float getSize() {
        return size.get();
    }

    public void setSize(float size) {
        this.size.set(size);
    }

    public String getFormula() {
        return formula.get();
    }

    public void setFormula(String formula) {
        this.formula.set(formula);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResizable() {
        return true;
    }

    private static class LatexStyleableFloatProperty extends StyleableFloatProperty {
        private final LatexView view;

        private LatexStyleableFloatProperty(float size,LatexView view) {
            super(size);
            this.view = view;
        }

        @Override
        public void invalidated() {
            view.update();
        }

        @Override
        public Object getBean() {
            return view;
        }

        @Override
        public String getName() {
            return "size";
        }

        @Override
        public CssMetaData<LatexView, Number> getCssMetaData() {
            return LatexView.StyleableProperties.SIZE;
        }
    }

    /**
     * Super-lazy instantiation pattern from Bill Pugh.
     * @treatAsPrivate implementation detail
     */
    private static class StyleableProperties {
        private static final CssMetaData<LatexView,Number> SIZE =
                new CssMetaData<LatexView,Number>("-fx-font-size", SizeConverter.getInstance(), DEFAULT_SIZE) {

                    @Override
                    public boolean isSettable(LatexView node) {
                        return node.size == null || !node.size.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(LatexView node) {
                        return node.size;
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>();
            styleables.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}
