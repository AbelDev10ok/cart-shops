package app.cart.shops.cart_shops.constrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.cart.shops.cart_shops.dto.ImageDto;
import app.cart.shops.cart_shops.exceptions.ImageException;
import app.cart.shops.cart_shops.models.Image;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.image.IIMageService;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RequiredArgsConstructor
@RestController
@RequestMapping("${value.api.prefix}/images")
public class ImageController {
    private final IIMageService imageService;

    
    @PostMapping("/upload")
    public ResponseEntity<?> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId) {
        for (MultipartFile file : files) {
            if (file.getContentType() == null || file.getContentType().isEmpty()) {
                // Manejar el error, por ejemplo, devolver un mensaje al cliente.
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Tipo MIME no especificado para el archivo: " + file.getOriginalFilename()));
            }
        }
        
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("success",imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImages(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);

    // almacena la imagen en bytes
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
    
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
            .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> putMethodName(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image!= null){
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("success",image));
            }
            
        } catch (ImageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),"image not found"));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("update failde!","something went wrong"));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image!= null){
                imageService.deleteImageById(imageId);;
                return ResponseEntity.ok(new ApiResponse("Delete success",image));
            }
            
        } catch (ImageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),"image not found"));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failde!","something went wrong"));
    }

}
