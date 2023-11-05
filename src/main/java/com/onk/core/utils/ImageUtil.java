package com.onk.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImageUtil {

    /**
     * png image format
     */
    public static final String IMAGE_FORMAT_PNG = "png";

    /**
     * jpg image format
     */
    public static final String IMAGE_FORMAT_JPG = "jpg";

    /**
     * jpeg image format
     */
    public static final String IMAGE_FORMAT_JPEG = "jpeg";

    /**
     * tiff image format
     *
     * @since 1.2.1
     */
    public static final String IMAGE_FORMAT_TIFF = "tiff";

    /**
     * Image quality best
     */
    public static final float QUALITY_BEST = 0.7f;

    /**
     * Image quality medium
     */
    public static final float QUALITY_MEDIUM = 0.5f;

    /**
     * Image quality minimum
     */
    public static final float QUALITY_MINIMUM = 0.3f;

    /**
     * Image mime type JPG
     */
    public static final String MIMETYPE_JPG = "image/jpeg";

    /**
     * Image mime type PNG
     */
    public static final String MIMETYPE_PNG = "image/png";

    /**
     * Create a buffered image for the dimensions and image format
     *
     * @param width       image width
     * @param height      image height
     * @param imageFormat image format
     * @return image
     */
    public static BufferedImage createBufferedImage(int width, int height,
                                                    String imageFormat) {

        int imageType;

        switch (imageFormat.toLowerCase()) {
            case IMAGE_FORMAT_JPG:
            case IMAGE_FORMAT_JPEG:
                imageType = BufferedImage.TYPE_INT_RGB;
                break;
            default:
                imageType = BufferedImage.TYPE_INT_ARGB;
        }

        return new BufferedImage(width, height, imageType);
    }

    /**
     * Check if the image is fully transparent, meaning it contains only
     * transparent pixels as an empty image
     *
     * @param image image
     * @return true if fully transparent
     */
    public static boolean isFullyTransparent(BufferedImage image) {
        boolean transparent = true;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                transparent = isTransparent(image, x, y);
                if (!transparent) {
                    break;
                }
            }
            if (!transparent) {
                break;
            }
        }
        return transparent;
    }

    /**
     * Check if the pixel in the image at the x and y is transparent
     *
     * @param image image
     * @param x     x location
     * @param y     y location
     * @return true if transparent
     */
    public static boolean isTransparent(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        return (pixel >> 24) == 0x00;
    }

    /**
     * Get a buffered image of the MultipartFile
     *
     * @param imageFile MultipartFile
     * @return buffered image or null
     * @since 1.1.2
     */
    public static BufferedImage getImage(MultipartFile imageFile) {
        BufferedImage image = null;
        try {
            if (imageFile != null && imageFile.getSize() > 0) {
                if (IMAGE_FORMAT_PNG.equals(formatName(imageFile.getOriginalFilename()))) {
                    image =  pngToJpg(imageFile.getBytes());
                } else {
                    InputStream stream = new ByteArrayInputStream(imageFile.getBytes());
                    image = ImageIO.read(stream);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("getImage: " + e.getMessage() + " " + e);
        }
        return image;
    }

    /**
     * Get a buffered image of the image bytes
     *
     * @param imageByte byte[]
     * @return buffered image or null
     * @since 1.1.2
     */
    public static BufferedImage getImage(byte[] imageByte) {
        try {
            return ImageIO.read(new ByteArrayInputStream(imageByte));
        } catch (IOException e) {
            throw new IllegalArgumentException("getImage: " + e.getMessage() + " " + e);
        }
    }

    /**
     * Write the image to bytes in the provided format and optional quality
     *
     * @param image      buffered image
     * @param formatName image format name
     * @param quality    null or quality between 0.0 and 1.0
     * @return image bytes
     * @since 1.1.2
     */
    public static byte[] writeImageToBytes(BufferedImage image,
                                           String formatName, Float quality) {
        byte[] bytes = null;
        if (quality != null) {
            bytes = compressAndWriteImageToBytes(image, formatName, quality);
        } else {
            bytes = writeImageToBytes(image, formatName);
        }
        return bytes;
    }

    /**
     * Write the image to bytes in the provided format
     *
     * @param image      buffered image
     * @param formatName image format name
     * @return image bytes
     * @since 1.1.2
     */
    public static byte[] writeImageToBytes(BufferedImage image,
                                           String formatName) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, formatName, stream);
            stream.flush();
            byte[] bytes = stream.toByteArray();
            stream.close();
            return bytes;
        } catch (IOException ioe) {
            throw new IllegalArgumentException("writeImageToBytes: " + ioe.getMessage() + " " + ioe);
        }
    }

    /**
     * Compress and write the image to bytes in the provided format and quality
     *
     * @param image      buffered image
     * @param formatName image format name
     * @param quality    quality between 0.0 and 1.0
     * @return compressed image bytes
     * @since 1.1.2
     */
    public static byte[] compressAndWriteImageToBytes(BufferedImage image,
                                                      String formatName, float quality) {

        byte[] bytes = null;

        Iterator<ImageWriter> writers = ImageIO
                .getImageWritersByFormatName(formatName);
        if (writers == null || !writers.hasNext()) {
            throw new IllegalArgumentException(
                    "No Image Writer to compress format: " + formatName);
        }
        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        if (writeParam.canWriteCompressed()) {
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            if (writeParam.getCompressionType() == null) {
                writeParam.setCompressionType(writeParam.getCompressionTypes()[0]);
            }
            writeParam.setCompressionQuality(quality);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), writeParam);
            writer.dispose();
            bytes = baos.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to compress image to format: " + formatName
                            + ", with quality: " + quality, e);
        } finally {
            closeQuietly(ios);
            closeQuietly(baos);
        }

        return bytes;
    }

    public static BufferedImage resize(BufferedImage img, float scale) {
        Image tmp = img.getScaledInstance( (int) (img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage((int) (img.getWidth(null) * scale), (int)(img.getHeight(null) * scale), img.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public static BufferedImage resize(BufferedImage img, int maxWidth, int maxHeight, String formatName) {
        int scaledWidth = 0, scaledHeight = 0;

        try {
            scaledWidth = maxWidth;
            scaledHeight = (int) (img.getHeight() * ((double) scaledWidth / img.getWidth()));

            if (scaledHeight > maxHeight) {
                scaledHeight = maxHeight;
                scaledWidth = (int) (img.getWidth() * ((double) scaledHeight / img.getHeight()));
                if (scaledWidth > maxWidth) {
                    scaledWidth = maxWidth;
                    scaledHeight = maxHeight;
                }
            }
            Image resized = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage buffered = new BufferedImage(scaledWidth, scaledHeight, Image.SCALE_REPLICATE);
            buffered.getGraphics().drawImage(resized, 0, 0, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffered, formatName, out);
            InputStream is = new ByteArrayInputStream(out.toByteArray());
            return ImageIO.read(is);
        } catch (Exception e) {
            throw new IllegalArgumentException("resize : " + e.getMessage() + " " + e);
        }
    }

    /**
     * PNG image convert JPG image
     *
     * @param pngImage PNG image file content byte
     * @return JPG format image
     */
    public static BufferedImage pngToJpg(byte[] pngImage) {
        BufferedImage image = getImage(pngImage);
        if (image == null) return null;
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param imageName the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or <code>null</code>
     * if the filename is <code>null</code>.
     */
    public static String formatName(String imageName) {
        return FilenameUtils.getExtension(imageName);
    }

    /**
     * Close quietly
     *
     * @param closeable closeable
     * @since 1.1.2
     */
    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("closeQuietly: " + e.getMessage() + " " + e);
            }
        }
    }

}
