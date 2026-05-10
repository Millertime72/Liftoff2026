package pkgETTranslator;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MiddleMan {

    private static BufferedReader runCommand(String subcommand, List<String> arguments) throws IOException {
        List<String> command = new ArrayList<>();
        
		Path frontendRoot = Paths.get(System.getProperty("user.dir"));
		Path backendPath = frontendRoot.resolve("../backend/venv/Scripts/python.exe").normalize();

        command.add(backendPath.toAbsolutePath().toString());
        command.add("-u"); // Make output occur in real time
        command.add("-m");
        command.add("et_translator");
        command.add(subcommand);

        // Add optional arguments
        command.addAll(arguments);

        ProcessBuilder pb = new ProcessBuilder(command);

        pb.redirectErrorStream(true);

        Process process = pb.start();

        return new BufferedReader(
            new InputStreamReader(
                process.getInputStream(),
                StandardCharsets.UTF_8
            )
        );
    }

    public static String speechToText(Integer duration) throws IOException {
        List<String> arguments = List.of("--duration", duration.toString());
        BufferedReader reader = runCommand("speech_to_text", arguments);

        // Wait for first line
        String statusLine = reader.readLine();

        if (statusLine == null) {
            throw new IOException("Python process ended before recording started.");
        }

        System.out.println("Speak now!");

        // Wait for second line
        String resultLine = reader.readLine();

        if (resultLine == null) {
            throw new IOException("Python process ended before returning transcription.");
        }

        return resultLine;
    }

    public static ArrayList<String> textToImages(String text, Integer nImages) throws IOException {
        List<String> arguments = List.of(text, "--n_images", nImages.toString());
        BufferedReader reader = runCommand("text_to_images", arguments);
        String line;
        ArrayList<String> lines = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
}