# 🔐 Instrucciones para Obtener el Token Correcto de Instagram

## ⚠️ Problema Detectado

Tu token actual es un **token de usuario**, pero necesitas un **token de página** con permisos de Instagram.

## ✅ Solución: Generar Token con Permisos Correctos

### **Paso 1: Ir al Graph API Explorer**

👉 https://developers.facebook.com/tools/explorer/

### **Paso 2: Configurar el Explorer Correctamente**

En la parte superior verás estos controles:

```
┌─────────────────────────────────────────────────────────┐
│ [Meta App ▼]  [User or Page ▼]  [Permissions ▼]  [v18.0 ▼] │
└─────────────────────────────────────────────────────────┘
```

**Configura así:**

1. **Meta App**: Selecciona tu app (ID: 813439077768151)

2. **User or Page**: 
   - Haz clic aquí
   - Cambia de "User Token" a **"Page Access Token"**
   - Selecciona tu **Página de Facebook** (debe estar vinculada a Instagram)

3. **Permissions**: Haz clic en "Add a Permission" y marca:
   ```
   ✅ instagram_basic
   ✅ instagram_manage_messages
   ✅ pages_messaging
   ✅ pages_manage_metadata
   ✅ pages_read_engagement
   ```

### **Paso 3: Generar el Token**

1. Haz clic en **"Generate Access Token"**
2. Acepta todos los permisos
3. **Copia el token completo** (empieza con `EAA...`)

### **Paso 4: (Opcional) Convertir a Token de Larga Duración**

El token que generas dura 1-2 horas. Para convertirlo a uno de 60 días:

```bash
curl "https://graph.facebook.com/v18.0/oauth/access_token?\
grant_type=fb_exchange_token&\
client_id=813439077768151&\
client_secret=TU_APP_SECRET&\
fb_exchange_token=TU_TOKEN_TEMPORAL"
```

**Obtener App Secret:**
- Ve a: https://developers.facebook.com/apps/813439077768151/settings/basic/
- Haz clic en "Show" en "App Secret"
- Cópialo

---

## 📋 Requisitos Previos

### **¿Tienes una Página de Facebook?**

Si no tienes una página de Facebook:

1. Ve a https://www.facebook.com/pages/create
2. Crea una página de negocio
3. Vincúlala con tu cuenta de Instagram Business

### **¿Tu Instagram es una Cuenta Business?**

Tu cuenta de Instagram debe ser:
- ✅ **Instagram Business Account** (no personal)
- ✅ Vinculada a una Página de Facebook
- ✅ Con mensajería habilitada

**Convertir a Business:**
1. Abre Instagram en tu móvil
2. Ve a Configuración → Cuenta
3. Selecciona "Cambiar a cuenta profesional"
4. Elige "Empresa" 
5. Vincula con tu Página de Facebook

---

## 🔍 Verificar la Configuración Actual

### **Verificar si tienes páginas:**

```bash
curl "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_TOKEN" | jq '.'
```

**Resultado esperado:**
```json
{
  "data": [
    {
      "id": "123456789",
      "name": "Mi Página",
      "access_token": "EAA..."  ← Este es el token que necesitas!
    }
  ]
}
```

### **Verificar vinculación con Instagram:**

```bash
curl "https://graph.facebook.com/v18.0/TU_PAGE_ID?fields=instagram_business_account&access_token=TU_TOKEN" | jq '.'
```

**Resultado esperado:**
```json
{
  "instagram_business_account": {
    "id": "17841405793187218"  ← Este es tu Instagram Page ID
  }
}
```

---

## 🎯 Resumen Rápido

**Lo que necesitas:**

1. ✅ Página de Facebook
2. ✅ Instagram Business vinculado a esa página  
3. ✅ Token de PÁGINA (no de usuario)
4. ✅ Permisos: `instagram_manage_messages`, `pages_messaging`

**Luego:**
```bash
# Configurar credenciales
./setup-env.sh

# O manualmente:
cat > .env << 'EOF'
INSTAGRAM_ACCESS_TOKEN=TU_PAGE_ACCESS_TOKEN_AQUI
INSTAGRAM_VERIFY_TOKEN=instagram_verify_01fc042c51fc71fe716df639c7e48f99
INSTAGRAM_PAGE_ID=TU_INSTAGRAM_BUSINESS_ACCOUNT_ID
SERVER_PORT=8080
EOF

# Cargar y reiniciar
export $(cat .env | xargs)
mvn spring-boot:run
```

---

## 📚 Links Útiles

- **Graph API Explorer**: https://developers.facebook.com/tools/explorer/
- **Tu App Dashboard**: https://developers.facebook.com/apps/813439077768151/
- **Docs Instagram Messaging**: https://developers.facebook.com/docs/messenger-platform/instagram
- **Convertir a Business Account**: https://help.instagram.com/502981923235522

---

**Última actualización**: Octubre 2025  
**App ID**: 813439077768151

