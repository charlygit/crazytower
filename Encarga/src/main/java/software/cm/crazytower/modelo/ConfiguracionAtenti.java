package software.cm.crazytower.modelo;

import java.util.List;


public class ConfiguracionAtenti {
    private List<String> urlsVideo;
    private List<String> urlsImagen;
    private String urlImagenInicio;
    private String urlImagenFin;

    public List<String> getUrlsVideo() {
        return urlsVideo;
    }

    public ConfiguracionAtenti setUrlsVideo(List<String> urlsVideo) {
        this.urlsVideo = urlsVideo;
        return this;
    }

    public List<String> getUrlsImagen() {
        return urlsImagen;
    }

    public ConfiguracionAtenti setUrlsImagen(List<String> urlsImagen) {
        this.urlsImagen = urlsImagen;
        return this;
    }

    public String getUrlImagenInicio() {
        return urlImagenInicio;
    }

    public ConfiguracionAtenti setUrlImagenInicio(String urlImagenInicio) {
        this.urlImagenInicio = urlImagenInicio;
        return this;
    }

    public String getUrlImagenFin() {
        return urlImagenFin;
    }

    public ConfiguracionAtenti setUrlImagenFin(String urlImagenFin) {
        this.urlImagenFin = urlImagenFin;
        return this;
    }
}
