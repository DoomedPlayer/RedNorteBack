package BinarySeint.BFF.dto;

public class DocumentoDTO {

    private String nombreDocumento; 
    private String emisorYFecha;    
    private String urlDescarga;     

    public DocumentoDTO() {
    }

    public DocumentoDTO(String nombreDocumento, String emisorYFecha, String urlDescarga) {
        this.nombreDocumento = nombreDocumento;
        this.emisorYFecha = emisorYFecha;
        this.urlDescarga = urlDescarga;
    }


    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getEmisorYFecha() {
        return emisorYFecha;
    }

    public void setEmisorYFecha(String emisorYFecha) {
        this.emisorYFecha = emisorYFecha;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
}