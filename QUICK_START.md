# ⚡ Inicio Rápido - Instagram Messaging API

Guía rápida para tener el sistema funcionando en menos de 10 minutos.

## 🚀 Pasos Rápidos

### 1. Verificar Prerequisitos

```bash
# Verificar Java
java -version  # Debe ser 17+

# Verificar Maven
mvn -version   # Debe estar instalado
```

### 2. Configurar Credenciales

```bash
# Copia la plantilla de variables de entorno
cp env-template.txt .env

# Edita el archivo .env con tus credenciales
# (Obtener credenciales en: https://developers.facebook.com/)
nano .env  # o usa tu editor favorito
```

Tu archivo `.env` debe verse así:

```env
INSTAGRAM_ACCESS_TOKEN=EAAxxxxxxxxxxxxxxxxxx
INSTAGRAM_VERIFY_TOKEN=mi_token_secreto_12345
INSTAGRAM_PAGE_ID=17841405793187218
SERVER_PORT=8080
```

### 3. Cargar Variables de Entorno

```bash
# Linux/Mac
export $(cat .env | xargs)

# Verificar
echo $INSTAGRAM_ACCESS_TOKEN
```

### 4. Compilar y Ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

Verás algo como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Started InstagramMessagingApplication in 3.456 seconds
```

### 5. Configurar Webhook (Solo para recibir mensajes)

#### Opción A: Desarrollo Local con ngrok

```bash
# Terminal 1: Mantén la app corriendo
mvn spring-boot:run

# Terminal 2: Exponer con ngrok
ngrok http 8080
```

Copia la URL HTTPS de ngrok (ej: `https://abc123.ngrok.io`)

#### Opción B: Configurar en Meta

1. Ve a [Meta for Developers](https://developers.facebook.com/apps/)
2. Selecciona tu app → **Messenger** → **Settings** → **Webhooks**
3. Agrega Callback URL: `https://abc123.ngrok.io/webhook`
4. Verify Token: El mismo que pusiste en `.env`
5. Suscríbete a: `messages`

### 6. Probar la Integración

```bash
# Test 1: Health check
curl http://localhost:8080/webhook/health

# Test 2: Enviar mensaje (reemplaza RECIPIENT_ID)
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "INSTAGRAM_USER_ID",
    "text": "¡Hola desde la API!"
  }'

# Test 3: Ejecutar todos los tests
./curl-examples.sh
```

## ✅ Checklist de Verificación

- [ ] Java 17+ instalado
- [ ] Maven instalado
- [ ] Credenciales de Instagram configuradas en `.env`
- [ ] Variables de entorno cargadas
- [ ] Aplicación compilada con `mvn clean install`
- [ ] Aplicación corriendo con `mvn spring-boot:run`
- [ ] Health check funcionando
- [ ] (Opcional) ngrok configurado para webhooks
- [ ] (Opcional) Webhook configurado en Meta

## 🎯 Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/webhook/health` | Health check del webhook |
| GET | `/api/messages/health` | Health check del controlador |
| GET | `/webhook` | Verificación del webhook (Meta) |
| POST | `/webhook` | Recepción de mensajes (Meta) |
| POST | `/api/messages/send` | Enviar mensaje a Instagram |

## 📝 Ejemplo de Envío de Mensaje

```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "123456789",
    "text": "¡Hola! 👋"
  }'
```

**Respuesta exitosa:**

```json
{
  "status": "success",
  "message": "Mensaje enviado correctamente",
  "response": "{\"recipient_id\":\"123456789\",\"message_id\":\"mid.xxx\"}"
}
```

## 🐛 Problemas Comunes

### Error: "Variables de entorno no configuradas"

```bash
# Solución: Cargar las variables
export $(cat .env | xargs)
```

### Error: "Puerto 8080 en uso"

```bash
# Solución 1: Cambiar puerto en .env
export SERVER_PORT=8081

# Solución 2: Matar proceso en puerto 8080
lsof -ti:8080 | xargs kill -9
```

### Error: "Invalid access token"

**Solución:**
1. Ve a [Graph API Explorer](https://developers.facebook.com/tools/explorer/)
2. Genera un nuevo token
3. Convierte a token de larga duración (ver SETUP_GUIDE.md)
4. Actualiza en `.env`

## 📚 Próximos Pasos

1. **Lee la documentación completa**: `README.md`
2. **Configura webhooks**: `SETUP_GUIDE.md`
3. **Prueba los ejemplos**: `./curl-examples.sh`
4. **Integra con tu sistema de mensajería unificada**

## 🆘 ¿Necesitas Ayuda?

- **Documentación completa**: `README.md`
- **Guía de configuración**: `SETUP_GUIDE.md`
- **Ejemplos de cURL**: `curl-examples.sh`
- **Meta Developers**: https://developers.facebook.com/docs/messenger-platform/instagram

---

**Equipo 3 - Instagram Integration** | UADE Seminario 2025


