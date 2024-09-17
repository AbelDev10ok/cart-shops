package app.cart.shops.cart_shops.services.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.cart.shops.cart_shops.dto.ImageDto;
import app.cart.shops.cart_shops.exceptions.ImageException;
import app.cart.shops.cart_shops.exceptions.ProductNotFoundException;
import app.cart.shops.cart_shops.models.Image;
import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.repositories.IImageRepository;
import app.cart.shops.cart_shops.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IIMageService{

    private final IImageRepository imageRepository;
    private final IProductRepository productRepository;


    @Override
    public void deleteImageById(Long id) {          
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()->{
            throw new ImageException("Image not found");
        });
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()->{
            throw new ImageException("Image not found");
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->{
            throw new ProductNotFoundException("Product not found");
        });
        List<ImageDto> saveImagesDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                // nombre de la imagen
                image.setFileName(file.getOriginalFilename());
                // png, jpg, jpeg, etc
                image.setFileType(file.getContentType());
                // imagen
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // esta seria la ruta de donde podemos obtener la image (postman)
                String downloadUrl = "/api/v1/images/image/download/" + image.getId();
                image.setDownloadUrl(downloadUrl);

                Image saveImage = imageRepository.save(image);
                saveImage.setDownloadUrl( "/api/v1/images/image/download/" + saveImage.getId());
                
                imageRepository.save(saveImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(saveImage.getId());
                imageDto.setImageName(saveImage.getFileName());
                imageDto.setDownloadUrl(saveImage.getDownloadUrl());
                saveImagesDto.add(imageDto);
                    
            } catch (IOException | SQLException e) {
                throw new ImageException("Error saving image");
            }
        }
        return saveImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new ImageException("Error updating image");
        }
        productRepository.findById(image.getProduct().getId()).orElseThrow(()->{
            throw new ProductNotFoundException("Product not found");
        });
    }

}
