# ⚡ Configuración Rápida - Instagram API

## 🎯 Objetivo

Configurar credenciales para conectar tu API con Instagram.

---

## 📋 Checklist de Configuración

### ✅ Paso 1: Verify Token (Ya listo)

```
✅ Token generado: instagram_verify_01fc042c51fc71fe716df639c7e48f99
```

---

### 🔄 Paso 2: Obtener Access Token

#### Opción A: Manual (Recomendado)

1. **Ir a Graph API Explorer**  
   👉 https://developers.facebook.com/tools/explorer/

2. **Configurar:**
   - Selecciona tu App
   - Selecciona tu Página de Facebook
   - Genera Access Token

3. **Permisos necesarios:**
   ```
   ✓ instagram_basic
   ✓ instagram_manage_messages
   ✓ pages_messaging
   ✓ pages_manage_metadata
   ```

4. **Convertir a token de larga duración:**
   ```bash
   curl "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id=TU_APP_ID&client_secret=TU_APP_SECRET&fb_exchange_token=TU_TOKEN_TEMPORAL"
   ```

#### Opción B: Asistente Interactivo

```bash
./setup-credentials.sh
```

---

### 🔄 Paso 3: Obtener Instagram Page ID

**Comando 1: Obtener Page ID de Facebook**
```bash
curl "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_ACCESS_TOKEN"
```

**Comando 2: Obtener Instagram Business Account ID**
```bash
curl "https://graph.facebook.com/v18.0/TU_PAGE_ID?fields=instagram_business_account&access_token=TU_ACCESS_TOKEN"
```

---

### 📝 Paso 4: Crear archivo .env

**Opción A: Script automático**
```bash
./setup-credentials.sh
```

**Opción B: Manual**
```bash
cat > .env << 'EOF'
INSTAGRAM_ACCESS_TOKEN=TU_TOKEN_AQUI
INSTAGRAM_VERIFY_TOKEN=instagram_verify_01fc042c51fc71fe716df639c7e48f99
INSTAGRAM_PAGE_ID=TU_PAGE_ID_AQUI
SERVER_PORT=8080
EOF
```

---

### 🚀 Paso 5: Ejecutar la Aplicación

```bash
# Cargar variables
export $(cat .env | xargs)

# Ejecutar app
mvn spring-boot:run
```

---

### 🌐 Paso 6: Configurar Webhook (Opcional para recibir mensajes)

**1. Exponer servidor local:**
```bash
ngrok http 8080
```

**2. Configurar en Meta:**
- URL: https://developers.facebook.com/apps/
- Ve a: Messenger → Settings → Webhooks
- Callback URL: `https://TU_URL.ngrok.io/webhook/instagram`
- Verify Token: `instagram_verify_01fc042c51fc71fe716df639c7e48f99`
- Eventos: `messages`

---

## 🧪 Probar la Configuración

```bash
# Test completo
./test-api.sh

# Enviar mensaje de prueba
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "INSTAGRAM_USER_ID",
    "message": "Hola desde la API!"
  }'
```

---

## 📚 Documentación Completa

| Archivo | Descripción |
|---------|-------------|
| `CREDENCIALES_INSTAGRAM.md` | Guía detallada paso a paso |
| `setup-credentials.sh` | Asistente interactivo |
| `SETUP_GUIDE.md` | Guía completa de setup |
| `README.md` | Documentación general |

---

## 🔗 Links Importantes

- **Meta for Developers**: https://developers.facebook.com/apps/
- **Graph API Explorer**: https://developers.facebook.com/tools/explorer/
- **Instagram Messaging Docs**: https://developers.facebook.com/docs/messenger-platform/instagram
- **ngrok Download**: https://ngrok.com/download

---

## ❓ Preguntas Frecuentes

### ¿Necesito tener una cuenta de Instagram Business?

✅ **Sí**, tu cuenta de Instagram debe ser:
- Instagram Business Account
- Vinculada a una Página de Facebook
- Con acceso a mensajería

### ¿Puedo probar sin configurar webhook?

✅ **Sí**, puedes:
- Enviar mensajes
- Simular recepción de mensajes con cURL
- Probar todos los endpoints localmente

Para **recibir mensajes reales** de Instagram, necesitas webhook configurado.

### ¿El Access Token expira?

⚠️ **Sí**:
- Token temporal: 1-2 horas
- Token de larga duración: ~60 días
- Debes convertir el temporal a larga duración (ver guía)

### ¿Puedo enviar mensajes a cualquier usuario?

⚠️ **No**:
- El usuario **debe haberte enviado un mensaje primero** (regla de Instagram)
- Rate limit: 100 mensajes/hora por usuario
- Solo mensajes de texto y algunos tipos de attachments

---

## 🆘 Ayuda

Si tienes problemas, revisa:
1. `CREDENCIALES_INSTAGRAM.md` - Guía detallada
2. Los logs de la aplicación: `mvn spring-boot:run`
3. Webhook logs en Meta Developer Console

---

**Última actualización**: Octubre 2025  
**Equipo 3 - Instagram Integration** | UADE

