package es.studium.practicatema9;

public class Tienda {
    private int idTienda;
    private String nombreTienda;

    // Constructor, getters y setters

    public Tienda()
    {
        idTienda = 0;
        nombreTienda = "";
    }

    public Tienda(String nombreTienda, int idTienda)
    {
        this.nombreTienda = nombreTienda;
        this.idTienda = idTienda;
    }

    public int getIdTienda()
    {
        return idTienda;
    }

    public void setIdTienda(int idTienda)
    {
        this.idTienda = idTienda;
    }

    public String getNombreTienda()
    {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda)
    {
        this.nombreTienda = nombreTienda;
    }
}
