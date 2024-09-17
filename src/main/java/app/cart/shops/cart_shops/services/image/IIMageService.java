package app.cart.shops.cart_shops.services.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import app.cart.shops.cart_shops.dto.ImageDto;
import app.cart.shops.cart_shops.models.Image;
/*
 multipartfile representa un archivo subido recibido en una aplicación Spring MVC.
 proporciona métodos para acceder al contenido del archivo, 
 como el nombre original del archivo, el tipo de contenido y el tamaño.
 */
public interface IIMageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    // nesecitmaos el id ya que cuando la imagen se crea la guardamos en el producto correcto
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
    

}
