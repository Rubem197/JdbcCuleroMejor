import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;
import java.util.Set;

public class conectarBD {

    public static void main(String[] args) {


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://dns11036.phdns11.es:3306/ad2223_rlindes?" +
                    "user=rlindes&password=0166";
            Connection con = DriverManager.getConnection(url);
            System.out.println(con.toString());
            conectar(con);

            borrarTabla(con, "DROP TABLE IF EXISTS Post");
            borrarTabla(con, "DROP TABLE IF EXISTS Likes");
            borrarTabla(con, "DROP TABLE IF EXISTS Usuarios");

            crearTabla(con, "CREATE TABLE IF NOT EXISTS Usuarios " + "(idUsuarios INTEGER, " + "Nombre VARCHAR(45), " +
                    "Apellido VARCHAR(45), " + "Username VARCHAR(40), " + "Password VARCHAR(45), " + "Email VARCHAR(50), " +
                    "PRIMARY KEY (idUsuarios))");

            crearTabla(con, "CREATE TABLE IF NOT EXISTS Likes " + "(idLikes INTEGER, " + "idUsuarios INTEGER, " + "idPost INTEGER, "
                    + "PRIMARY KEY (idLikes), "+ "FOREIGN KEY (idUsuarios) REFERENCES Usuarios(idUsuarios))");

            crearTabla(con, "CREATE TABLE IF NOT EXISTS Post " + "(idPost INTEGER, " + "idUsuarios INTEGER, " + "created_at DATE, "
                            + "updated_at DATE, "
                            + "PRIMARY KEY (idPost), "+ "FOREIGN KEY (idUsuarios) REFERENCES Usuarios(idUsuarios))");


            insertarDatos(con, "Usuarios.txt");
            insertarDatos(con, "Likes.txt");
            insertarDatos(con, "Post.txt");


            listarDatosUsuarios(con);
            listarDatosLikes(con);
            listarDatosPost(con);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void conectar(Connection con) throws SQLException {

        Statement st = con.createStatement();
        String usar = "USE ad2223_rlindes";
        st.execute(usar);
    }

    public static void crearTabla(Connection con, String tabla) throws SQLException {

        Statement st = con.createStatement();

        st.execute(tabla);

    }

    public static void insertarDatos(Connection con, String fichero) throws SQLException, FileNotFoundException {
        Statement st = con.createStatement();
        BufferedReader br = new BufferedReader(new FileReader(fichero));
        Scanner sc = new Scanner(br);
        String linea;
        while (sc.hasNext()){
            linea=sc.nextLine();
            st.execute(linea);
        }
    }

    public static void listarDatosUsuarios(Connection con){

        Statement smtl = null;
        ResultSet resultSet=null;
        try {

            smtl = con.createStatement();
            resultSet = smtl.executeQuery("SELECT * FROM Usuarios");

            while(resultSet.next()){
                    System.out.print("id" + resultSet.getInt("idUsuarios"));
                    System.out.print(", Nombre " + resultSet.getString("Nombre"));
                    System.out.print(", Apellido " + resultSet.getString("Apellido"));
                    System.out.println(", Username " + resultSet.getString("Username"));
                    System.out.println(", Contraseña " + resultSet.getString("Password"));
                    System.out.println(", Email " + resultSet.getString("Email"));

                System.out.println();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarDatosLikes(Connection con){

        Statement smtl = null;
        ResultSet resultSet=null;
        try {

            smtl = con.createStatement();
            resultSet = smtl.executeQuery("SELECT * FROM Likes");

            while(resultSet.next()){
                System.out.print("idLikes" + resultSet.getInt("idLikes"));
                System.out.print(", idUsuarios " + resultSet.getInt("idUsuarios"));
                System.out.print(", idPost " + resultSet.getInt("idPost"));

                System.out.println();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarDatosPost(Connection con){

        Statement smtl = null;
        ResultSet resultSet=null;
        try {

            smtl = con.createStatement();
            resultSet = smtl.executeQuery("SELECT * FROM Post");

            while(resultSet.next()){
                System.out.print("idPost" + resultSet.getInt("idPost"));
                System.out.print(", idUsuarios " + resultSet.getInt("idUsuarios"));
                System.out.print(", creado " + resultSet.getDate("created_at"));
                System.out.print(", actualizado " + resultSet.getDate("updated_at"));

                System.out.println();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public static void borrarTabla(Connection con, String tabla) throws SQLException {

        Statement st = con.createStatement();


        st.execute(tabla);
    }

}
