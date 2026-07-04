# Back Ventas - Spring Boot API REST

API REST desarrollada con Spring Boot para gestionar ventas. El servicio permite crear, listar, buscar, actualizar y eliminar ventas, persistiendo la información en MySQL. Además incluye documentación Swagger/OpenAPI, configuración Docker y manifiestos Kubernetes para despliegue.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Bean Validation
- MySQL 8.4
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven Wrapper
- Docker / Docker Compose
- Kubernetes / EKS
- H2 para pruebas

## Estructura principal

```text
Springboot-API-REST/
├── src/main/java/com/citt/
│   ├── controller/          # Controladores REST
│   ├── persistence/entity/   # Entidades JPA
│   ├── persistence/repository/ # Repositorios Spring Data
│   ├── persistence/services/ # Lógica de negocio
│   ├── exceptions/           # Manejo de errores
│   └── config/               # Configuración Swagger/OpenAPI
├── src/main/resources/
│   ├── application.properties
│   ├── application-docker.properties
│   └── application-test.properties
├── k8s/                      # Manifiestos Kubernetes
├── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
└── pom.xml
```

## Funcionalidades

- Crear ventas.
- Listar todas las ventas.
- Buscar una venta por ID.
- Actualizar una venta existente.
- Eliminar una venta.
- Validar campos obligatorios como dirección, fecha y estado de despacho generado.
- Exponer documentación Swagger para probar endpoints.

## Modelo de datos: Venta

La entidad principal es `Venta` y contiene los siguientes campos:

| Campo | Tipo | Descripción |
|---|---|---|
| `idVenta` | Long | Identificador único generado automáticamente. |
| `direccionCompra` | String | Dirección asociada a la compra. Campo obligatorio. |
| `valorCompra` | int | Valor total de la compra. |
| `fechaCompra` | LocalDate | Fecha de compra. Campo obligatorio. |
| `despachoGenerado` | Boolean | Indica si la venta ya tiene despacho generado. |

## Endpoints principales

Base URL local:

```text
http://localhost:8081/api/v1/ventas
```

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/ventas` | Lista todas las ventas. |
| `GET` | `/api/v1/ventas/{idVenta}` | Obtiene una venta por ID. |
| `POST` | `/api/v1/ventas` | Crea una nueva venta. |
| `PUT` | `/api/v1/ventas/{idVenta}` | Actualiza una venta existente. |
| `DELETE` | `/api/v1/ventas/{idVenta}` | Elimina una venta. |

## Ejemplos de uso

### Crear una venta

```bash
curl -X POST http://localhost:8081/api/v1/ventas \
  -H "Content-Type: application/json" \
  -d '{
    "direccionCompra": "Av. Providencia 1234, Santiago",
    "valorCompra": 45990,
    "fechaCompra": "2026-07-04",
    "despachoGenerado": false
  }'
```

### Listar ventas

```bash
curl http://localhost:8081/api/v1/ventas
```

### Buscar venta por ID

```bash
curl http://localhost:8081/api/v1/ventas/1
```

### Actualizar una venta

```bash
curl -X PUT http://localhost:8081/api/v1/ventas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "direccionCompra": "Av. Las Condes 1450, Santiago",
    "valorCompra": 67990,
    "fechaCompra": "2026-07-04",
    "despachoGenerado": true
  }'
```

### Eliminar una venta

```bash
curl -X DELETE http://localhost:8081/api/v1/ventas/1
```

## Requisitos previos

Para ejecutar el proyecto localmente se necesita:

- Java 17 instalado.
- Maven o Maven Wrapper incluido en el proyecto.
- MySQL disponible, o Docker instalado para levantar la base de datos automáticamente.

## Variables de entorno

La configuración principal usa variables de entorno para conectarse a MySQL:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_ENDPOINT` | Host de la base de datos. | `localhost` |
| `DB_PORT` | Puerto de MySQL. | `3307` |
| `DB_NAME` | Nombre de la base de datos. | `ventasdb` |
| `DB_USERNAME` | Usuario de MySQL. | `ventasuser` |
| `DB_PASSWORD` | Contraseña de MySQL. | `ventaspass` |

En Docker se usan estas variables:

| Variable | Valor por defecto |
|---|---|
| `MYSQL_DATABASE` | `ventasdb` |
| `MYSQL_USER` | `ventasuser` |
| `MYSQL_PASSWORD` | `ventaspass` |
| `MYSQL_ROOT_PASSWORD` | `rootpass123` |
| `SPRING_PROFILES_ACTIVE` | `docker` |

## Ejecución local con Maven

Desde la carpeta raíz del proyecto:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

El servicio quedará disponible en:

```text
http://localhost:8081
```

## Ejecución con Docker Compose

El archivo `docker-compose.yml` levanta MySQL y el backend.

```bash
docker compose up --build -d
```

Ver contenedores:

```bash
docker ps
```

Ver logs del backend:

```bash
docker logs backend-ventas -f
```

Detener servicios:

```bash
docker compose down
```

Eliminar también los datos persistidos:

```bash
docker compose down -v
```

Con Docker Compose, los puertos principales son:

| Servicio | Puerto local | Puerto interno |
|---|---:|---:|
| Backend ventas | `8081` | `8080` |
| MySQL ventas | `3307` | `3306` |

## Swagger / OpenAPI

La documentación de la API está disponible en:

```text
http://localhost:8081/swagger-ui.html
```

Desde ahí se pueden probar los endpoints sin usar Postman o curl.

## Pruebas

El proyecto incluye pruebas con Spring Boot Test y H2 para ambiente de test.

Ejecutar pruebas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

## Despliegue en Kubernetes / EKS

El proyecto incluye manifiestos en la carpeta `k8s/`:

```text
k8s/
├── mysql-ventas-deployment.yaml
├── mysql-ventas-secret.yaml
├── mysql-ventas-service.yaml
├── ventas-deployment.yaml
├── ventas-hpa.yaml
└── ventas-service.yaml
```

Aplicar manifiestos:

```bash
kubectl apply -f k8s/
```

Ver recursos:

```bash
kubectl get pods -n innovatech
kubectl get svc -n innovatech
kubectl get hpa -n innovatech
```

El servicio de ventas queda expuesto internamente dentro del cluster como:

```text
ventas-service:8080
```

## Imagen Docker

Construir imagen local:

```bash
docker build -t backend-ventas:latest .
```

Ejecutar imagen manualmente:

```bash
docker run -p 8081:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3307/ventasdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=ventasuser \
  -e SPRING_DATASOURCE_PASSWORD=ventaspass \
  backend-ventas:latest
```

## Notas importantes

- El proyecto utiliza `spring.jpa.hibernate.ddl-auto=update`, por lo que Hibernate crea o actualiza las tablas automáticamente.
- La base de datos principal es `ventasdb`.
- El endpoint `/api/v1/ventas` también se usa como ruta de health check en Kubernetes.
- Si se ejecuta junto al frontend en Kubernetes, Nginx redirige `/api/v1/ventas` hacia `ventas-service:8080`.
