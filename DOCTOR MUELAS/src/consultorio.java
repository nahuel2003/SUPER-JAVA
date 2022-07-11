import java.sql.*;
import java.util.*;
public class consultorio {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/doctormuelas";
	static final String USER = "root";
	static final String PASS = "44914466Nahuel";
	
	public static void main(String[] args) {
		dibujarCuadrado("-",9,2);
		System.out.println("BIENVENIDOS A DOCTOR MUELAS!");
		dibujarCuadrado("-",9,2);
		//PARAMETROS
		Connection conn = null;
		Scanner sc = new Scanner(System.in);
		////////////////////////////////////
		System.out.println("CONECTANDO A LA BASE DE DATOS...");
		// INTENTANDO ESTABLECER CONEXION CON LA BASE DE DATOS DEL CONSULTORIO
		///////////////////////////////////////////////////////////
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("-  -  -  CONECTADO!  -  -  -");
			dibujarCuadrado("-",9,3);
			System.out.println(
			"|-------------------------------|\n" +
			"|----------DOCTOR--MUELAS-------|\n" +
			"|-------------------------------|\n" +
			"|-BIENVENIDO!-------------------|\n" +
			"|-------------------------------|\n" +
			"|	1) INICIAR SESION\n" +
			"|	2) REGISTRARSE\n" +
			"|	3) SALIR\n" +
			"|			\n" +
			"|-SELECCIONE UNA OPCIÓN: ");
			///// DETERMINAR CUAL SERA EL SIGUIENTE MENU /////
			try{
				int accionInicio = sc.nextInt();
				
				switch(accionInicio){
					default:
						System.out.println("OPCION NO VALIDA, ESCOJA OTRA");
					case 1:
						MenuInicioSesion(conn);
					break;
					case 2:
						MenuRegistrarUsuario(conn, false);
					break;
				}
			}catch(Exception ex){
				System.out.println("OPCION NO VALIDA, ESCOJA OTRA");
				Reintentar(conn);
			}
		}catch(SQLException error){
			System.out.println("ERROR AL CONECTARSE A LA BASE DE DATOS :(");
		}
	}
	public static void Reintentar(Connection conn){
		Scanner sc = new Scanner(System.in);
		try{
			int accionInicio = sc.nextInt();
			
			switch(accionInicio){
				default:
					System.out.println("OPCION NO VALIDA, ESCOJA OTRA");
				case 1:
					MenuInicioSesion(conn);
				break;
				case 2:
					MenuRegistrarUsuario(conn, false);
				break;
			}
		}catch(Exception ex){
			System.out.println("OPCION NO VALIDA, ESCOJA OTRA");
			Reintentar(conn);
		}
	}
	public static void MenuInicioSesion(Connection conn){
		String nombreUsuario = "";
		String clave = "";
		Scanner sc = new Scanner(System.in);
		dibujarCuadrado("-", 9, 10);
		System.out.println(
		"|===============================|\n" +
		"|*INICIAR SESION****************|\n" +
		"|===============================|\n" +
		"|-NOMBRE DE USUARIO:	...");
		nombreUsuario = sc.nextLine();
		if(nombreUsuario != null){
			System.out.println(
			"-CONTRASEÑA:	...");
			clave = sc.nextLine();
			ComprobarCredenciales(conn, nombreUsuario, clave);
		}
	}
	public static void ComprobarCredenciales(Connection conn, String usuario, String clave){
		dibujarCuadrado("-", 9, 1);
		System.out.println("COMPROBANDO CREDENCIALES...");
		dibujarCuadrado("-", 9, 2);
		try{
			Statement stmt = conn.createStatement();
			String consulta = "SELECT *from registro where usuario = '" + usuario + "' and clave = '" + clave + "'";
			ResultSet rs = stmt.executeQuery(consulta);
			
			if(rs.next()){
				dibujarCuadrado("*", 9, 4);
				System.out.println("SESION INICIADA CORRECTAMENTE");
				MenuPrincipal();
			}else{
				dibujarCuadrado("-", 9, 10);
				System.out.println(
				"Error al iniciar sesion, usuario no encontrado o contraseña incorrecta\n" +
				"|========================|\n" +
				"|-DESEA CREAR UNA CUENTA?\n" +
				"|========================|\n" +
				"|	1) SI\n" +
				"|	2) NO, Reintentar\n");
				Scanner sc = new Scanner(System.in);
				int accionDeUsuario = sc.nextInt();
				switch(accionDeUsuario){
					default:
						System.out.println("OPCION NO VALIDA, ESCOJA OTRA");
					break;
					case 1:
						stmt.close();
						rs.close();
						MenuRegistrarUsuario(conn, false);
					break;
					case 2:
						MenuInicioSesion(conn);
					break;
				}
			}
		}catch(SQLException e){
			System.out.println("Error al iniciar sesion: " + e);
		}
	}
	public static void MenuRegistrarUsuario(Connection conn, boolean usuarioYaExistente){
		dibujarCuadrado("-", 9, 10);
		Scanner sc = new Scanner(System.in);
		String nuevoUsuario = "";
		if(usuarioYaExistente){
			System.out.println("ESTE NOMBRE DE USUARIO ESTÁ EN USO, ESCOJA OTRO");
			System.out.println(
			"|-------------------------------|\n" +
			"|----------DOCTOR--MUELAS-------|\n" +
			"|-------------------------------|\n" +
			"|-REGISTRO DE USUARIO-----------|\n" +
			"|-------------------------------|\n" +
			"|	NOMBRE DE USUARIO:	...\n");
		}else{
			dibujarCuadrado("-",9,6);
			System.out.println(
			"|-------------------------------|\n" +
			"|----------DOCTOR--MUELAS-------|\n" +
			"|-------------------------------|\n" +
			"|-REGISTRO DE USUARIO-----------|\n" +
			"|-------------------------------|\n" +
			"|	NOMBRE DE USUARIO:	...\n");
		}
		nuevoUsuario = sc.nextLine();
		
		try{
			String consultarSiUsuarioExiste = "SELECT *from registro where usuario = '" + nuevoUsuario + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(consultarSiUsuarioExiste);
			if(rs.next()){
				stmt.close();
				rs.close();
				MenuRegistrarUsuario(conn, true);
			}else{
				String[] clavesValidadas = ValidarClavesDeUsuario();
				while(!clavesValidadas[0].equals(clavesValidadas[1])){
					System.out.println("LAS CONTRASEÑAS NO COINCIDEN, INTENTALO DE NUEVO");
					clavesValidadas = ValidarClavesDeUsuario();
				}
				RegistrarEnBaseDeDatos(false, conn, nuevoUsuario, clavesValidadas[0]);
			}
		}catch(SQLException error){
			System.out.println("ERROR AL INTENTAR REGISTRAR USUARIO");
		}
	}
	public static String[] ValidarClavesDeUsuario(){
		String[] claves = new String[2];
		Scanner sc = new Scanner(System.in);
		System.out.println(
		"|	CONTRASEÑA:	...");
		claves[0] = sc.nextLine();
		System.out.println(
		"|	REPETIR CONTRASEÑA:	...");
		claves[1] = sc.nextLine();
		return claves;
	}
	public static void RegistrarEnBaseDeDatos(boolean datosIncorrectos, Connection conn, String nuevoUsuario, String clave){
		if(datosIncorrectos){
			System.out.println("INGRESA LOS DATOS CORRECTAMENTE");
		}else{
			System.out.println("PERFECTO! AHORA INGRESA ALGUNOS DATOS ESPECIFICOS...");
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("NOMBRE DE PILA:	...");
		String nombre = sc.nextLine();
		System.out.println("APELLIDO:	...");
		String apellido = sc.nextLine();
		System.out.println("NÚMERO DE DOCUMENTO (DNI):	...");
		try{
			int dni = sc.nextInt();
			dibujarCuadrado("-",9,7);
			System.out.println(
			"GENIAL! ESTOS DATOS SON CORRECTOS?\n" +
			"----------------------------------\n" +
			"NOMBRE DE USUARIO: " + nuevoUsuario +"\n" +
			"NOMBRE Y APELLIDO: " + nombre + " " + apellido +"\n" +
			"DNI:               " + dni + "\n" +
			"|	1) SI\n" +
			"|	2) NO, Reintentar\n");
			int accionUsuario = sc.nextInt();
			switch(accionUsuario){
			default:
				System.out.println("Opcion no valida, ingresa otro numero");
				break;
			case 1:
				dibujarCuadrado("-", 9, 10);
				System.out.println("INICIANDO REGISTRO...");
				String registrarUsuario = "insert into registro values (null, '"+nuevoUsuario+"','"+nombre+"','"+apellido+"',"+dni+",null,'" + clave + "', 'cliente')";
			
				try{
					Statement stmt = conn.createStatement();
					stmt.executeUpdate(registrarUsuario);
					System.out.println("REGISTRADO CON EXITO!");
					dibujarCuadrado("*", 9, 1);
					MenuPrincipal();
				}catch(SQLException error){
					System.out.println("OCCURIO UN PROBLEMA AL REGISTRAR LOS DATOS, INTENTALO DE NUEVO");
					RegistrarEnBaseDeDatos(true, conn, nuevoUsuario, clave);
				}
				break;
			case 2:
				RegistrarEnBaseDeDatos(true, conn, nuevoUsuario, clave);
				break;
			}
		}catch(Exception errorDeTipeo){
			System.out.println("SOLO NÚMEROS EN EL DNI");
			RegistrarEnBaseDeDatos(true, conn, nuevoUsuario, clave);
		}
	}
	public static void MenuPrincipal(){
		dibujarCuadrado("-", 9, 3);
		System.out.println(
				"|-------------------------------|\n" +
				"|----------DOCTOR--MUELAS-------|\n" +
				"|-------------------------------|\n" +
				"|-INICIO------------------------|\n" +
				"|-------------------------------|\n" +
				"|	1) TURNOS DISPONIBLES\n" +
				"|	2) MI CUENTA\n" +
				"|	3) SALIR\n" +
				"|			\n" +
				"|-SELECCIONE UNA OPCIÓN: ");
	}
	// METODO PARA DEVOLVER UN PRINT DE CUALQUIER CARACTER
	//CON FORMA DE RECTANGULO CON EL ANCHO Y ALTO ESPEFICICADO
	public static void dibujarCuadrado(String caracter, int ancho, int alto){
		for(int i=1;i<=alto;i++){ 
			for(int j=1;j<=ancho;j++){ 
				System.out.print(" "+ caracter + " ");
			} System.out.println(" ");
		}
	}
	//FUENTES https://www.centrosdentalplus.es/las-consultas-tratamientos-mas-comunes-la-consulta-del-dentista/
}
