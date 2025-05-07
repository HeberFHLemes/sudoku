import br.com.sudoku.ui.custom.screen.MainScreen;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class MainUI {

    public static void main(String[] args) {

        // Argumentos passados como parâmetros para execução do main
        final var gameConfig = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1])
                );

        var mainsScreen = new MainScreen(gameConfig);
        mainsScreen.buildMainScreen();
    }
}

