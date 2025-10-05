#!/bin/bash

# Script interactivo para configurar credenciales de Instagram

echo "🔐 Configurador de Credenciales - Instagram Messaging API"
echo "=========================================================="
echo ""

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Función para leer input con valor por defecto
read_with_default() {
    local prompt=$1
    local default=$2
    local value
    
    if [ -n "$default" ]; then
        read -p "$prompt [$default]: " value
        echo "${value:-$default}"
    else
        read -p "$prompt: " value
        echo "$value"
    fi
}

echo -e "${BLUE}Este asistente te ayudará a configurar las credenciales de Instagram.${NC}"
echo ""
echo "📚 Si necesitas ayuda detallada, consulta: CREDENCIALES_INSTAGRAM.md"
echo ""

# Verificar si ya existe .env
if [ -f .env ]; then
    echo -e "${YELLOW}⚠️  Ya existe un archivo .env${NC}"
    read -p "¿Quieres sobrescribirlo? (s/N): " overwrite
    if [[ ! $overwrite =~ ^[Ss]$ ]]; then
        echo "Cancelado. Archivo .env existente preservado."
        exit 0
    fi
    echo ""
fi

echo -e "${GREEN}Paso 1: Verify Token${NC}"
echo "───────────────────────────────────────────────"
echo "Este es un token secreto que tú defines para verificar webhooks."
echo ""
VERIFY_TOKEN="instagram_verify_01fc042c51fc71fe716df639c7e48f99"
echo -e "${GREEN}✅ Token generado automáticamente: ${VERIFY_TOKEN}${NC}"
echo ""

echo -e "${YELLOW}Paso 2: Access Token${NC}"
echo "───────────────────────────────────────────────"
echo "Necesitas obtener el Page Access Token de Meta Developer Portal."
echo ""
echo "Pasos rápidos:"
echo "1. Ve a: https://developers.facebook.com/tools/explorer/"
echo "2. Selecciona tu app y página"
echo "3. Genera token con permisos: instagram_manage_messages, pages_messaging"
echo "4. Convierte a token de larga duración (ver CREDENCIALES_INSTAGRAM.md)"
echo ""
ACCESS_TOKEN=$(read_with_default "Ingresa tu Access Token" "")

while [ -z "$ACCESS_TOKEN" ] || [ "$ACCESS_TOKEN" == "your_page_access_token_here" ]; do
    echo -e "${RED}❌ Access Token requerido${NC}"
    ACCESS_TOKEN=$(read_with_default "Ingresa tu Access Token" "")
done
echo ""

echo -e "${YELLOW}Paso 3: Instagram Page ID${NC}"
echo "───────────────────────────────────────────────"
echo "Este es el ID de tu Instagram Business Account."
echo ""
echo "Para obtenerlo, ejecuta:"
echo "curl \"https://graph.facebook.com/v18.0/me/accounts?access_token=\$ACCESS_TOKEN\""
echo ""
PAGE_ID=$(read_with_default "Ingresa tu Instagram Page ID" "")

while [ -z "$PAGE_ID" ] || [ "$PAGE_ID" == "your_instagram_business_account_id_here" ]; do
    echo -e "${RED}❌ Page ID requerido${NC}"
    PAGE_ID=$(read_with_default "Ingresa tu Instagram Page ID" "")
done
echo ""

echo -e "${YELLOW}Paso 4: Puerto del Servidor${NC}"
echo "───────────────────────────────────────────────"
SERVER_PORT=$(read_with_default "Puerto del servidor" "8080")
echo ""

# Crear archivo .env
echo -e "${GREEN}Creando archivo .env...${NC}"

cat > .env << EOF
# Instagram API Configuration
# Generado: $(date)
# NO SUBIR ESTE ARCHIVO A GIT

# Page Access Token de Meta
INSTAGRAM_ACCESS_TOKEN=$ACCESS_TOKEN

# Token de verificación para webhooks
INSTAGRAM_VERIFY_TOKEN=$VERIFY_TOKEN

# Instagram Business Account ID
INSTAGRAM_PAGE_ID=$PAGE_ID

# Puerto del servidor
SERVER_PORT=$SERVER_PORT
EOF

echo -e "${GREEN}✅ Archivo .env creado exitosamente${NC}"
echo ""

# Mostrar resumen
echo "════════════════════════════════════════════════"
echo -e "${GREEN}📋 Resumen de Configuración${NC}"
echo "════════════════════════════════════════════════"
echo ""
echo "VERIFY_TOKEN: $VERIFY_TOKEN"
echo "ACCESS_TOKEN: ${ACCESS_TOKEN:0:20}..."
echo "PAGE_ID: $PAGE_ID"
echo "SERVER_PORT: $SERVER_PORT"
echo ""

# Cargar variables
echo -e "${BLUE}¿Quieres cargar las variables en la sesión actual? (s/N):${NC}"
read load_vars

if [[ $load_vars =~ ^[Ss]$ ]]; then
    export $(cat .env | xargs)
    echo -e "${GREEN}✅ Variables cargadas${NC}"
    echo ""
    echo "Puedes verificar con:"
    echo "  echo \$INSTAGRAM_ACCESS_TOKEN"
    echo ""
fi

echo "════════════════════════════════════════════════"
echo -e "${GREEN}🎉 Configuración Completa${NC}"
echo "════════════════════════════════════════════════"
echo ""
echo "Próximos pasos:"
echo ""
echo "1️⃣  Cargar variables (si no lo hiciste):"
echo "   export \$(cat .env | xargs)"
echo ""
echo "2️⃣  Ejecutar la aplicación:"
echo "   mvn spring-boot:run"
echo ""
echo "3️⃣  Configurar webhook con ngrok:"
echo "   ngrok http 8080"
echo "   URL Callback: https://TU_URL.ngrok.io/webhook/instagram"
echo "   Verify Token: $VERIFY_TOKEN"
echo ""
echo "4️⃣  Probar la API:"
echo "   ./test-api.sh"
echo ""
echo "📚 Para más información: CREDENCIALES_INSTAGRAM.md"
echo ""

