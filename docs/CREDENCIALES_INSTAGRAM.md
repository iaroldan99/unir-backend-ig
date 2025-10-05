# 🔐 Guía para Obtener Credenciales de Instagram

## 📝 Credenciales que necesitas:

1. ✅ **VERIFY_TOKEN**: `instagram_verify_01fc042c51fc71fe716df639c7e48f99` (Ya generado)
2. ⏳ **ACCESS_TOKEN**: Obtener de Meta Developer Portal
3. ⏳ **PAGE_ID**: Instagram Business Account ID

---

## 🚀 Paso 1: Crear App en Meta for Developers

### 1.1. Ir a Meta for Developers

👉 **URL**: https://developers.facebook.com/apps/

### 1.2. Crear o Seleccionar una App

- Si no tienes app: Haz clic en **"Create App"**
  - Tipo: **Business** o **None**
  - Nombre: `Mi App de Mensajería UADE` (o el que prefieras)
  - Email de contacto: Tu email

- Si ya tienes app: Selecciónala

### 1.3. Agregar Producto Instagram

1. En el dashboard de tu app
2. Busca **"Add Product"**
3. Encuentra **"Instagram"** → **"Set Up"**
4. También agrega **"Messenger"** si no está

---

## 🎯 Paso 2: Obtener el ACCESS TOKEN

### 2.1. Ir al Graph API Explorer

👉 **URL**: https://developers.facebook.com/tools/explorer/

### 2.2. Generar el Token

1. **Selecciona tu App** en el dropdown superior
2. Haz clic en **"Generate Access Token"**
3. **Selecciona tu Página de Facebook** (la que está vinculada a Instagram)
4. **Autoriza los permisos**:
   - ✅ `instagram_basic`
   - ✅ `instagram_manage_messages`
   - ✅ `pages_messaging`
   - ✅ `pages_manage_metadata`
   - ✅ `pages_read_engagement`

5. **Copia el token** que aparece (es temporal, dura 1-2 horas)

### 2.3. Convertir a Token de Larga Duración

⚠️ **IMPORTANTE**: El token temporal expira rápido. Conviértelo:

**Necesitas:**
- `APP_ID`: Lo encuentras en **Settings → Basic → App ID**
- `APP_SECRET`: En **Settings → Basic → App Secret** (haz clic en "Show")
- `TEMP_TOKEN`: El token que acabas de copiar del Explorer

**Comando:**

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id=TU_APP_ID&client_secret=TU_APP_SECRET&fb_exchange_token=TU_TOKEN_TEMPORAL"
```

**Ejemplo real:**

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id=123456789&client_secret=abcd1234efgh5678&fb_exchange_token=EAAxxxxxxxxxxxxx"
```

**Respuesta esperada:**

```json
{
  "access_token": "EAAxxxxxxxxxxxxx...",
  "token_type": "bearer"
}
```

**Guarda este `access_token` - es tu token de larga duración!** ✅

---

## 📊 Paso 3: Obtener el Instagram Page ID

### 3.1. Obtener ID de tu Página de Facebook

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_ACCESS_TOKEN"
```

**Busca en la respuesta:**

```json
{
  "data": [
    {
      "id": "123456789012345",  ← Este es tu PAGE_ID
      "name": "Tu Página"
    }
  ]
}
```

### 3.2. Obtener Instagram Business Account ID

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/123456789012345?fields=instagram_business_account&access_token=TU_ACCESS_TOKEN"
```

**Respuesta esperada:**

```json
{
  "instagram_business_account": {
    "id": "17841405793187218"  ← Este es tu INSTAGRAM_PAGE_ID ✅
  }
}
```

---

## 📝 Paso 4: Crear el archivo .env

Crea el archivo `.env` en la raíz del proyecto con este contenido:

```env
# Instagram API Configuration
INSTAGRAM_ACCESS_TOKEN=EAAxxxxxxxxxxxxx
INSTAGRAM_VERIFY_TOKEN=instagram_verify_01fc042c51fc71fe716df639c7e48f99
INSTAGRAM_PAGE_ID=17841405793187218
SERVER_PORT=8080
```

**Comandos:**

```bash
# Crear archivo .env
cat > .env << 'EOF'
INSTAGRAM_ACCESS_TOKEN=PEGA_TU_TOKEN_AQUI
INSTAGRAM_VERIFY_TOKEN=instagram_verify_01fc042c51fc71fe716df639c7e48f99
INSTAGRAM_PAGE_ID=PEGA_TU_PAGE_ID_AQUI
SERVER_PORT=8080
EOF

# Cargar las variables
export $(cat .env | xargs)

# Verificar
echo $INSTAGRAM_ACCESS_TOKEN
```

---

## 🌐 Paso 5: Configurar Webhook (Para Recibir Mensajes)

### 5.1. Exponer tu servidor local

```bash
# Instalar ngrok si no lo tienes
# Mac: brew install ngrok
# O descarga: https://ngrok.com/download

# Exponer puerto 8080
ngrok http 8080
```

Copia la URL HTTPS (ej: `https://abc123.ngrok.io`)

### 5.2. Configurar en Meta

1. Ve a tu app en **Meta for Developers**
2. **Messenger** → **Settings** → **Webhooks**
3. **Add Callback URL**:
   - **Callback URL**: `https://abc123.ngrok.io/webhook/instagram`
   - **Verify Token**: `instagram_verify_01fc042c51fc71fe716df639c7e48f99`
4. Haz clic en **"Verify and Save"**

### 5.3. Suscribirse a Eventos

Marca estas opciones:
- ✅ `messages`
- ✅ `messaging_postbacks` (opcional)
- ✅ `message_echoes` (opcional)

### 5.4. Vincular Página

En **"Subscribe to Pages"**, selecciona tu página de Facebook.

---

## ✅ Paso 6: Probar la Configuración

```bash
# 1. Reiniciar la aplicación
mvn spring-boot:run

# 2. En otra terminal, ejecutar tests
./test-api.sh

# 3. Enviar mensaje de prueba a tu Instagram Business
# Desde otra cuenta de Instagram, envía un mensaje a tu cuenta Business
# Deberías ver el mensaje en los logs de tu aplicación
```

---

## 🔍 Verificar que Todo Funciona

### Health Checks

```bash
curl http://localhost:8080/webhook/instagram/health
curl http://localhost:8080/api/messages/health
curl http://localhost:8080/send/health
```

### Probar Envío de Mensaje

⚠️ **Nota**: El usuario debe haberte enviado un mensaje primero

```bash
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "INSTAGRAM_USER_ID",
    "message": "¡Hola desde la API!"
  }'
```

---

## 🐛 Problemas Comunes

### "Invalid OAuth access token"

**Solución**: Regenera el token siguiendo el Paso 2

### "Webhook verification failed"

**Solución**: 
- Verifica que el `VERIFY_TOKEN` en `.env` coincida con el configurado en Meta
- Asegúrate que la app esté corriendo
- Verifica que ngrok esté activo

### "Cannot send message"

**Solución**:
- El usuario debe haberte enviado un mensaje primero (regla de Instagram)
- Verifica que el `recipientId` sea correcto
- Verifica rate limits (100 mensajes/hora)

---

## 📚 Enlaces Útiles

- **Meta for Developers**: https://developers.facebook.com/
- **Graph API Explorer**: https://developers.facebook.com/tools/explorer/
- **Instagram Messaging Docs**: https://developers.facebook.com/docs/messenger-platform/instagram
- **Webhook Testing**: https://developers.facebook.com/tools/webhooks/
- **ngrok**: https://ngrok.com/

---

## 🎉 ¡Listo!

Una vez que hayas completado todos los pasos, tu API estará lista para:
- ✅ Recibir mensajes de Instagram
- ✅ Enviar mensajes a usuarios
- ✅ Procesar y normalizar mensajes
- ✅ Integrar con sistema de mensajería unificada

---

**Última actualización**: Octubre 2025  
**Equipo 3 - Instagram Integration** | UADE Seminario

