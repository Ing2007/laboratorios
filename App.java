package edu.umg;

import org.bson.Document;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductoDAO dao = new ProductoDAO();
        int opcion = -1;

        do {
            System.out.println("\n====================================");
            System.out.println("     TIENDA - MONGODB ATLAS");
            System.out.println("====================================");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto");
            System.out.println("4. Actualizar precio");
            System.out.println("5. Actualizar existencia");
            System.out.println("6. Eliminar producto");
            System.out.println("7. Productos con poco inventario");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(" Error: Debe ingresar un número entero válido.");
                continue;
            }

            switch (opcion) {
                case 1 -> {
                    System.out.println("\n--- AGREGAR PRODUCTO ---");
                    System.out.print("Código: ");
                    String codigo = scanner.nextLine().trim();

                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine().trim();

                    System.out.print("Categoría: ");
                    String categoria = scanner.nextLine().trim();

                    double precio = -1;
                    while (precio < 0) {
                        System.out.print("Precio: Q");
                        try {
                            precio = Double.parseDouble(scanner.nextLine());
                            if (precio < 0) System.out.println(" El precio no puede ser negativo.");
                        } catch (NumberFormatException e) {
                            System.out.println(" Ingrese un valor numérico válido.");
                        }
                    }

                    int existencia = -1;
                    while (existencia < 0) {
                        System.out.print("Existencia: ");
                        try {
                            existencia = Integer.parseInt(scanner.nextLine());
                            if (existencia < 0) System.out.println(" La existencia no puede ser negativa.");
                        } catch (NumberFormatException e) {
                            System.out.println(" Ingrese un número entero válido.");
                        }
                    }

                    Producto p = new Producto(codigo, nombre, categoria, precio, existencia);
                    dao.insertar(p);
                    System.out.println(" Producto guardado exitosamente.");
                }

                case 2 -> {
                    System.out.println("\n--- LISTA DE PRODUCTOS ---");
                    List<Document> productos = dao.listar();
                    if (productos.isEmpty()) {
                        System.out.println("No hay productos registrados en la base de datos.");
                    } else {
                        imprimirEncabezadoTabla();
                        for (Document doc : productos) {
                            imprimirFilaProducto(doc);
                        }
                    }
                }

                case 3 -> {
                    System.out.println("\n--- BUSCAR PRODUCTO ---");
                    System.out.print("Ingrese el código a buscar: ");
                    String codigo = scanner.nextLine().trim();
                    Document doc = dao.buscarPorCodigo(codigo);

                    if (doc != null) {
                        imprimirEncabezadoTabla();
                        imprimirFilaProducto(doc);
                    } else {
                        System.out.println(" Producto no encontrado con el código: " + codigo);
                    }
                }

                case 4 -> {
                    System.out.println("\n--- ACTUALIZAR PRECIO ---");
                    System.out.print("Código del producto: ");
                    String codigo = scanner.nextLine().trim();

                    if (dao.buscarPorCodigo(codigo) == null) {
                        System.out.println(" El código ingresado no existe en la base de datos.");
                        break;
                    }

                    double nuevoPrecio = -1;
                    while (nuevoPrecio < 0) {
                        System.out.print("Nuevo precio: Q");
                        try {
                            nuevoPrecio = Double.parseDouble(scanner.nextLine());
                            if (nuevoPrecio < 0) System.out.println(" El precio no puede ser negativo.");
                        } catch (NumberFormatException e) {
                            System.out.println(" Ingrese un valor numérico válido.");
                        }
                    }

                    if (dao.actualizarPrecio(codigo, nuevoPrecio)) {
                        System.out.println(" Precio actualizado correctamente.");
                    } else {
                        System.out.println(" No se pudo actualizar el precio.");
                    }
                }

                case 5 -> {
                    System.out.println("\n--- ACTUALIZAR EXISTENCIA ---");
                    System.out.print("Código del producto: ");
                    String codigo = scanner.nextLine().trim();

                    if (dao.buscarPorCodigo(codigo) == null) {
                        System.out.println(" El código ingresado no existe en la base de datos.");
                        break;
                    }

                    int nuevaExistencia = -1;
                    while (nuevaExistencia < 0) {
                        System.out.print("Nueva existencia: ");
                        try {
                            nuevaExistencia = Integer.parseInt(scanner.nextLine());
                            if (nuevaExistencia < 0) System.out.println(" La existencia no puede ser negativa.");
                        } catch (NumberFormatException e) {
                            System.out.println(" Ingrese un número entero válido.");
                        }
                    }

                    if (dao.actualizarExistencia(codigo, nuevaExistencia)) {
                        System.out.println(" Existencia actualizada correctamente.");
                    } else {
                        System.out.println(" No se pudo actualizar la existencia.");
                    }
                }

                case 6 -> {
                    System.out.println("\n--- ELIMINAR PRODUCTO ---");
                    System.out.print("Código del producto a eliminar: ");
                    String codigo = scanner.nextLine().trim();

                    if (dao.eliminar(codigo)) {
                        System.out.println(" Producto eliminado de MongoDB Atlas.");
                    } else {
                        System.out.println(" No se encontró ningún producto con el código: " + codigo);
                    }
                }

                case 7 -> {
                    System.out.println("\n--- PRODUCTOS CON POCO INVENTARIO ---");
                    System.out.print("Ingrese el límite máximo de existencia (ej. 5): ");
                    int limite = 5;
                    try {
                        limite = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Se usará el límite por defecto (5).");
                    }

                    List<Document> pocoStock = dao.listarPocoInventario(limite);
                    if (pocoStock.isEmpty()) {
                        System.out.println("No hay productos con menos de " + limite + " unidades.");
                    } else {
                        imprimirEncabezadoTabla();
                        for (Document doc : pocoStock) {
                            imprimirFilaProducto(doc);
                        }
                    }
                }

                case 0 -> System.out.println("Saliendo del programa... ¡Hasta luego!");

                default -> System.out.println(" Opción no válida. Por favor, seleccione entre 0 y 7.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void imprimirEncabezadoTabla() {
        System.out.printf("%-10s %-25s %-15s %-12s %-10s%n", "CÓDIGO", "NOMBRE", "CATEGORÍA", "PRECIO", "EXISTENCIA");
        System.out.println("--------------------------------------------------------------------------------");
    }

    private static void imprimirFilaProducto(Document doc) {
        System.out.printf("%-10s %-25s %-15s Q%-11.2f %-10d%n",
                doc.getString("codigo"),
                doc.getString("nombre"),
                doc.getString("categoria"),
                doc.getDouble("precio"),
                doc.getInteger("existencia"));
    }
}
