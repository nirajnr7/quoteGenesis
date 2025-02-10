package org.codingdsa.quotegenesis.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.codingdsa.quotegenesis.model.GeneratedQuote;
import org.codingdsa.quotegenesis.repository.GeneratedQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class QuoteImageGenerator {

    @Autowired
    private GeneratedQuoteRepository generatedQuoteRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    private static final String IMAGE_FOLDER = "output";
    private static final String FONT_PATH = "src/main/resources/static/font/PlayfairDisplay-Regular.ttf";
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1350;

    public void generateAndUploadQuoteImage() throws Exception {
        File imageFile = createQuoteImage();

        try {
            imageUploadService.uploadImageViaFreeImageHost(imageFile.getAbsolutePath());
        } finally {
            if (imageFile.exists() && imageFile.delete()) {
                System.out.println("Image file deleted successfully: " + imageFile.getAbsolutePath());
            } else {
                System.err.println("Failed to delete image file: " + imageFile.getAbsolutePath());
            }
        }
    }


    public File createQuoteImage() throws IOException {
        List<GeneratedQuote> quotes = generatedQuoteRepository.findLastQuote();
        if (quotes.isEmpty()) {
            throw new RuntimeException("No quotes found");
        }
        GeneratedQuote quote = quotes.get(0);

        // Create output directory if it doesn't exist
        String projectDir = System.getProperty("user.dir");
        Path imagePath = Paths.get(projectDir, "src", "main", "resources", IMAGE_FOLDER);
        if (!Files.exists(imagePath)) {
            Files.createDirectories(imagePath);
        }

        // Load custom font
        Font quoteFont;
        try {
            quoteFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH)).deriveFont(Font.BOLD, 48);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(quoteFont);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load custom font", e);
        }

        // Create image with white background (JPEG does not support transparency)
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Enable anti-aliasing for smooth text rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set background (JPEG requires an explicit background)
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Set text color and font
        g.setColor(new Color(51, 51, 51));
        g.setFont(quoteFont);

        // Get and validate quote text
        String quoteText = StringUtils.hasText(quote.getQuoteText())
                ? quote.getQuoteText().substring(1, quote.getQuoteText().lastIndexOf("\""))
                : "No quote text available";
        String authorName = StringUtils.hasText(quote.getAuthor()) ? "-" + quote.getAuthor() : "~Unknown";

        // Word wrap the quote
        FontMetrics metrics = g.getFontMetrics(quoteFont);
        List<String> lines = wrapText(quoteText, metrics, WIDTH - 200);

        // Draw quote text
        int lineHeight = metrics.getHeight();
        int totalHeight = lineHeight * lines.size();
        int y = (HEIGHT - totalHeight) / 2;

        for (String line : lines) {
            int x = (WIDTH - metrics.stringWidth(line)) / 2;
            g.drawString(line, x, y);
            y += lineHeight;
        }

        // Draw author name
        Font authorFont = quoteFont.deriveFont(Font.ITALIC, 36);
        g.setFont(authorFont);
        metrics = g.getFontMetrics(authorFont);
        int x = (WIDTH - metrics.stringWidth(authorName)) / 2;
        g.drawString(authorName, x, y + metrics.getHeight() + 40);

        g.dispose();

        // Save image as JPEG
        String fileName = String.format("quote_%d.jpg", quote.getId()); // Changed to .jpeg
        File outputFile = new File(imagePath.toString(), fileName);

        ImageIO.write(image, "jpg", outputFile); // Save as JPEG

        return outputFile;
    }

    private static List<String> wrapText(String text, FontMetrics metrics, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (metrics.stringWidth(testLine) > maxWidth) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line.append(line.length() == 0 ? word : " " + word);
            }
        }
        lines.add(line.toString()); // Add the last line
        return lines;
    }
}
