# 🚀 Guía Rápida: Enviar Mensaje de Instagram

## ✅ Prerequisitos

1. **Cuenta de Instagram Business** vinculada a una Página de Facebook
2. **App en Meta for Developers** 
3. **20 minutos** para configuración inicial

---

## 🔑 PASO 1: Obtener Access Token (5 min)

### 1.1. Ve al Graph API Explorer
👉 https://developers.facebook.com/tools/explorer/

### 1.2. Configura:
- **Aplicación**: Selecciona tu app (o crea una nueva)
- **Usuario o Página**: Selecciona tu Página de Facebook

### 1.3. Genera Token con Permisos:
Haz clic en **"Generate Access Token"** y autoriza estos permisos:
- ✅ `instagram_basic`
- ✅ `instagram_manage_messages`
- ✅ `pages_messaging`
- ✅ `pages_manage_metadata`

### 1.4. Copia el Token
Copia el token que aparece (empieza con `EAA...`)

⚠️ **IMPORTANTE**: Este token expira en 1-2 horas. Para convertirlo a larga duración:

```bash
curl "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id=TU_APP_ID&client_secret=TU_APP_SECRET&fb_exchange_token=TU_TOKEN_TEMPORAL"
```

Reemplaza:
- `TU_APP_ID`: En Meta → Settings → Basic → App ID
- `TU_APP_SECRET`: En Meta → Settings → Basic → App Secret (clic en "Show")
- `TU_TOKEN_TEMPORAL`: El token que copiaste

**Guarda el nuevo token de larga duración**

---

## 📊 PASO 2: Obtener Instagram Page ID (2 min)

### 2.1. Obtener ID de tu Página de Facebook

```bash
curl "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_ACCESS_TOKEN"
```

Busca el `"id"` de tu página.

### 2.2. Obtener Instagram Business Account ID

```bash
curl "https://graph.facebook.com/v18.0/TU_PAGE_ID?fields=instagram_business_account&access_token=TU_ACCESS_TOKEN"
```

Copia el `"id"` dentro de `instagram_business_account`.

---

## ⚙️ PASO 3: Configurar .env (1 min)

Edita el archivo `.env` en la raíz del proyecto:

```bash
nano .env
```

O ábrelo con tu editor favorito y cambia:

```env
INSTAGRAM_ACCESS_TOKEN=EAAxxxxxxxxxxxxx    # Tu token de larga duración
INSTAGRAM_VERIFY_TOKEN=instagram_verify_01fc042c51fc71fe716df639c7e48f99
INSTAGRAM_PAGE_ID=17841405793187218       # Tu IG Business Account ID
SERVER_PORT=8080
```

Guarda (Ctrl+O, Enter, Ctrl+X en nano).

---

## 🔄 PASO 4: Cargar Variables y Reiniciar (1 min)

```bash
# Cargar variables
export $(cat .env | xargs)

# Verificar
echo $INSTAGRAM_ACCESS_TOKEN

# Si la app está corriendo, detenla (Ctrl+C) y reinicia:
mvn spring-boot:run
```

---

## 📱 PASO 5: Obtener Recipient ID (3 min)

⚠️ **REGLA DE INSTAGRAM**: Solo puedes enviar mensajes a usuarios que **te hayan enviado un mensaje primero en las últimas 24 horas**.

### Opción A: Usuario te envía mensaje

1. Desde otra cuenta de Instagram, envía un mensaje a tu cuenta Business
2. Revisa los logs de tu aplicación:

```
📩 NUEVO MENSAJE DE INSTAGRAM
De: 123456789012345    ← Este es el RECIPIENT_ID
```

### Opción B: Obtener de conversaciones existentes

```bash
curl "https://graph.facebook.com/v18.0/me/conversations?platform=instagram&access_token=TU_ACCESS_TOKEN"
```

---

## 🚀 PASO 6: Enviar Mensaje (1 min)

### Usando cURL:

```bash
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "RECIPIENT_ID_AQUI",
    "message": "¡Hola! Este es mi primer mensaje desde la API 🚀"
  }'
```

### Usando Postman:

```
POST http://localhost:8080/send/instagram

Body (JSON):
{
  "recipient": "RECIPIENT_ID_AQUI",
  "message": "¡Hola desde la API!"
}
```

---

## ✅ Verificar que Funcionó

**Respuesta exitosa:**
```json
{
  "status": "success",
  "platform": "instagram",
  "message": "Mensaje enviado correctamente",
  "recipientId": "123456789",
  "response": "..."
}
```

**El usuario debería recibir tu mensaje en Instagram** 🎉

---

## 🐛 Problemas Comunes

### "Invalid OAuth access token"
- Token expirado → Genera uno nuevo de larga duración
- Token sin permisos → Verifica que tenga `instagram_manage_messages`

### "Cannot send message to this user"
- El usuario no te envió mensaje primero
- Han pasado más de 24 horas desde el último mensaje del usuario

### "Access token is missing"
- Variables no cargadas → `export $(cat .env | xargs)`
- App no reiniciada con nuevas variables

---

## 📝 Checklist Final

- [ ] Access Token obtenido y convertido a larga duración
- [ ] Instagram Page ID obtenido
- [ ] Archivo `.env` configurado
- [ ] Variables cargadas: `export $(cat .env | xargs)`
- [ ] App reiniciada: `mvn spring-boot:run`
- [ ] Usuario te envió mensaje primero
- [ ] Recipient ID obtenido de los logs
- [ ] Mensaje enviado con cURL o Postman
- [ ] ¡Mensaje recibido en Instagram! 🎉

---

## 🆘 Ayuda Rápida

**Ver logs en tiempo real:**
```bash
# En la terminal donde corre mvn spring-boot:run
# Verás cada mensaje que llegue
```

**Test rápido sin credenciales reales:**
```bash
./test-api.sh
```

**Documentación completa:**
- `CREDENCIALES_INSTAGRAM.md` - Guía detallada
- `SETUP_GUIDE.md` - Setup completo
- `CURL_COMMANDS.md` - Todos los comandos

---

**¿Listo?** Empieza por el **PASO 1** 👆

---

**Equipo 3 - Instagram Integration** | UADE Seminario 2025

