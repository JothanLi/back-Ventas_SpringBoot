# Backend de Ventas

API REST Spring Boot para administrar ventas y marcar cuándo una venta ya tiene una orden de despacho asociada.

## Tecnologías

- Java 17 y Spring Boot 3.4.4
- Spring Web, Validation, JPA y Actuator
- MySQL 8.4 en desarrollo y producción
- H2 para pruebas automatizadas
- Docker multietapa y Docker Compose
- Kubernetes/EKS con Deployment, Service, HPA y MySQL persistente mediante StatefulSet
- GitHub Actions: test, package, Docker build, Trivy, ECR, deploy y smoke test

## Estructura

```text
.github/workflows/deploy-eks.yml
back-Ventas_SpringBoot/Springboot-API-REST/
├── src/
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── k8s/
```

## Endpoints

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/v1/ventas` | Listar ventas |
| GET | `/api/v1/ventas/{id}` | Consultar venta |
| POST | `/api/v1/ventas` | Crear venta |
| PUT | `/api/v1/ventas/{id}` | Reemplazar datos de venta |
| PATCH | `/api/v1/ventas/{id}/despacho` | Cambiar solamente `despachoGenerado` |
| DELETE | `/api/v1/ventas/{id}` | Eliminar venta |
| GET | `/actuator/health/readiness` | Readiness probe |
| GET | `/actuator/health/liveness` | Liveness probe |
| GET | `/swagger-ui.html` | Swagger UI |

El endpoint PATCH evita que una actualización parcial deje `valorCompra` en cero.

## Ejecución local

```bash
cd back-Ventas_SpringBoot/Springboot-API-REST
cp .env.example .env
docker compose up --build -d
docker compose ps
```

- API: `http://localhost:8081/api/v1/ventas`
- Swagger: `http://localhost:8081/swagger-ui.html`
- MySQL: `localhost:3307`

Detener sin borrar datos:

```bash
docker compose down
```

Detener y eliminar el volumen:

```bash
docker compose down -v
```

## Pruebas

```bash
cd back-Ventas_SpringBoot/Springboot-API-REST
./mvnw clean test
```

En Windows:

```powershell
.\mvnw.cmd clean test
```

## Pipeline CI/CD

Se ejecuta en `main` y `deploy`. En Pull Request realiza pruebas y empaquetado. En Push además:

1. Construye la imagen.
2. Escanea vulnerabilidades críticas con Trivy.
3. Publica etiquetas `${GITHUB_SHA::7}` y `latest` en ECR.
4. Crea el namespace y el Secret desde GitHub Secrets.
5. Despliega MySQL como StatefulSet con PVC.
6. Despliega la API y el HPA.
7. Ejecuta un smoke test dentro del clúster.
8. Publica diagnóstico y logs si falla.

### GitHub Secrets requeridos

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_REGION
EKS_CLUSTER_NAME
VENTAS_DB_USER
VENTAS_DB_PASSWORD
VENTAS_DB_ROOT_PASSWORD
```

No se versionan contraseñas reales. `k8s/mysql-ventas-secret.example.yaml` es solamente una plantilla.

## Persistencia y escalabilidad

MySQL utiliza un StatefulSet y un `volumeClaimTemplates` de 5 GiB. La API utiliza requests/limits, rolling update, probes y un HPA entre 1 y 3 réplicas.
