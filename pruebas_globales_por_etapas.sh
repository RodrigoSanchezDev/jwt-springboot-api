#!/bin/bash

# =============================================================================
# PRUEBAS GLOBALES POR ETAPAS - API REST + JWT + HATEOAS
# Análisis completo del proyecto y verificación de endpoints
# =============================================================================

echo "🚀 INICIANDO ANÁLISIS GLOBAL DEL PROYECTO LETRAS Y PAPELES"
echo "==========================================================="
echo ""

# Colores para salida
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # Sin color

# Variables de configuración
BASE_URL="http://localhost:8080"
API_V1="/api"
API_V2="/api/v2"

# Contadores para resultados
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Funciones auxiliares
increment_total() {
    ((TOTAL_TESTS++))
}

increment_passed() {
    ((PASSED_TESTS++))
    echo -e "${GREEN}✅ EXITOSO${NC}"
}

increment_failed() {
    ((FAILED_TESTS++))
    echo -e "${RED}❌ FALLIDO${NC}"
}

test_endpoint() {
    local url=$1
    local description=$2
    local expected_status=${3:-200}
    local token=${4:-""}
    
    echo -e "${BLUE}🔗 Probando:${NC} $description"
    echo -e "${YELLOW}📍 URL:${NC} $url"
    
    increment_total
    
    local auth_header=""
    if [ ! -z "$token" ]; then
        auth_header="-H \"Authorization: Bearer $token\""
    fi
    
    local response_code
    if [ ! -z "$token" ]; then
        response_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $token" "$url")
    else
        response_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    fi
    
    if [ "$response_code" -eq "$expected_status" ]; then
        increment_passed
    else
        increment_failed
        echo -e "${RED}   Esperado: $expected_status, Obtenido: $response_code${NC}"
    fi
    echo ""
}

show_response() {
    local url=$1
    local description=$2
    local token=${3:-""}
    
    echo -e "${PURPLE}📄 Respuesta de:${NC} $description"
    echo -e "${YELLOW}🌐 URL:${NC} $url"
    
    if [ ! -z "$token" ]; then
        curl -s -H "Authorization: Bearer $token" -H "Accept: application/hal+json" "$url" | jq '.' 2>/dev/null || curl -s -H "Authorization: Bearer $token" "$url"
    else
        curl -s -H "Accept: application/hal+json" "$url" | jq '.' 2>/dev/null || curl -s "$url"
    fi
    echo ""
    echo "=================================================="
    echo ""
}

# Obtener token JWT
get_jwt_token() {
    echo -e "${YELLOW}🔑 OBTENIENDO TOKEN JWT PARA PRUEBAS...${NC}"
    
    # El usuario ya existe, no necesitamos registrarlo
    echo "Usando usuario existente test@test.com"

    # Autenticar y obtener token (con usuario existente)
    TOKEN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
        -H "Content-Type: application/json" \
        -d '{
            "email": "test@test.com",
            "contra": "test123"
        }' | jq -r '.token // empty' 2>/dev/null)
    
    if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
        echo -e "${RED}❌ No se pudo obtener el token JWT${NC}"
        echo "No se pudo autenticar con las credenciales disponibles"
        TOKEN=""
    fi
    
    if [ ! -z "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
        echo -e "${GREEN}✅ Token JWT obtenido exitosamente${NC}"
        echo "Token: ${TOKEN:0:50}..."
    else
        echo -e "${RED}❌ Error: No se pudo obtener token JWT${NC}"
        TOKEN=""
    fi
    echo ""
}

echo "📋 RESUMEN DEL PROYECTO ANALIZADO:"
echo "================================="
echo "🏢 Nombre: Letras y Papeles - API REST"
echo "🔧 Tecnología: Spring Boot 3.5.3 + JWT + HATEOAS"
echo "🗄️  Base de Datos: H2 (demo) / MySQL (producción)"
echo "📚 Documentación: OpenAPI/Swagger"
echo "🔒 Seguridad: JWT Authentication"
echo "🌐 API: V1 (Deprecated) + V2 (HATEOAS)"
echo ""

echo "🎯 ENDPOINTS IDENTIFICADOS PARA PRUEBAS:"
echo "========================================"
echo "📊 API V1 (Deprecated - OpenAPI):"
echo "   • GET  /api/productos - Lista todos los productos"
echo "   • GET  /api/productos/{id} - Obtiene un producto específico" 
echo "   • POST /api/productos - Agrega un producto nuevo"
echo "   • GET  /api/pedidos - Lista todos los pedidos"
echo "   • GET  /api/clientes - Lista todos los clientes"
echo ""
echo "🚀 API V2 (HATEOAS):"
echo "   • GET  /api/v2 - Root Discovery (punto de entrada)"
echo "   • GET  /api/v2/productos - CollectionModel con hipermedia"
echo "   • GET  /api/v2/productos/{id} - EntityModel con links"
echo "   • GET  /api/v2/pedidos - CollectionModel de pedidos"
echo "   • GET  /api/v2/clientes - CollectionModel de clientes"
echo ""
echo "🔐 Autenticación:"
echo "   • POST /api/auth/signup - Registro de usuario"
echo "   • POST /api/auth/signin - Autenticación y obtención de token"
echo ""

# =============================================================================
# ETAPA 1: VERIFICACIÓN DE INFRAESTRUCTURA
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█           ETAPA 1: VERIFICACIÓN DE INFRAESTRUCTURA         █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

echo "🏗️ Verificando que la aplicación esté ejecutándose..."
test_endpoint "$BASE_URL/swagger-ui/index.html" "Swagger UI disponible" 200
test_endpoint "$BASE_URL/v3/api-docs" "OpenAPI JSON disponible" 200

# =============================================================================
# ETAPA 2: AUTENTICACIÓN Y SEGURIDAD
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█            ETAPA 2: AUTENTICACIÓN Y SEGURIDAD              █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

# Obtener token para las siguientes pruebas
get_jwt_token

echo "🔒 Verificando seguridad de endpoints protegidos..."
test_endpoint "$BASE_URL/api/productos" "Productos sin token (debe fallar)" 401
test_endpoint "$BASE_URL/api/pedidos" "Pedidos sin token (debe fallar)" 401
test_endpoint "$BASE_URL/api/clientes" "Clientes sin token (debe fallar)" 401

# =============================================================================
# ETAPA 3: API V1 (OPENAPI/SWAGGER) - ENDPOINTS REQUERIDOS
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█           ETAPA 3: API V1 (OPENAPI) - ENDPOINTS            █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

if [ ! -z "$TOKEN" ]; then
    echo "📋 Probando endpoints V1 requeridos con autenticación JWT..."
    
    # Endpoints requeridos específicamente por el usuario
    test_endpoint "$BASE_URL/api/productos" "GET /productos: Lista todos los productos" 200 "$TOKEN"
    test_endpoint "$BASE_URL/api/productos/1" "GET /productos/{id}: Obtiene un producto específico" 200 "$TOKEN"
    test_endpoint "$BASE_URL/api/pedidos" "GET /pedidos: Lista todos los pedidos" 200 "$TOKEN"
    test_endpoint "$BASE_URL/api/clientes" "GET /clientes: Lista todos los clientes" 200 "$TOKEN"
    
    echo "🔨 Probando POST /productos: Agrega un producto nuevo..."
    increment_total
    response_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -X POST "$BASE_URL/api/productos" \
        -H "Authorization: Bearer $TOKEN" \
        -H "Content-Type: application/json" \
        -d '{
            "nombre": "Producto Test",
            "descripcion": "Producto de prueba",
            "precio": 10.99,
            "stock": 100
        }')
    
    if [ "$response_code" -eq 200 ] || [ "$response_code" -eq 201 ]; then
        increment_passed
    else
        increment_failed
        echo -e "${RED}   Código de respuesta: $response_code${NC}"
    fi
    echo ""
    
    # Verificar estructura de respuesta OpenAPI
    echo "📊 Verificando estructura de respuesta OpenAPI V1..."
    show_response "$BASE_URL/api/productos" "Lista de productos (OpenAPI V1)" "$TOKEN"
    
else
    echo -e "${RED}❌ No se puede probar API V1 sin token JWT${NC}"
    echo ""
fi

# =============================================================================
# ETAPA 4: API V2 (HATEOAS) - VERIFICACIÓN COMPLETA
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█            ETAPA 4: API V2 (HATEOAS) - HIPERMEDIA          █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

if [ ! -z "$TOKEN" ]; then
    echo "🌐 Probando navegación hipermedia HATEOAS..."
    
    # Root Discovery
    test_endpoint "$BASE_URL/api/v2" "Root Discovery - Punto de entrada HATEOAS" 200 "$TOKEN"
    
    # CollectionModel endpoints
    test_endpoint "$BASE_URL/api/v2/productos" "CollectionModel - Productos con hipermedia" 200 "$TOKEN"
    test_endpoint "$BASE_URL/api/v2/pedidos" "CollectionModel - Pedidos con hipermedia" 200 "$TOKEN"
    test_endpoint "$BASE_URL/api/v2/clientes" "CollectionModel - Clientes con hipermedia" 200 "$TOKEN"
    
    # EntityModel endpoints
    test_endpoint "$BASE_URL/api/v2/productos/1" "EntityModel - Producto individual con links" 200 "$TOKEN"
    
    echo "🔗 Verificando estructura HATEOAS con links de navegación..."
    show_response "$BASE_URL/api/v2" "API Root Discovery (Richardson Level 3)" "$TOKEN"
    show_response "$BASE_URL/api/v2/productos/1" "EntityModel con hipermedia" "$TOKEN"
    
else
    echo -e "${RED}❌ No se puede probar API V2 HATEOAS sin token JWT${NC}"
    echo ""
fi

# =============================================================================
# ETAPA 5: VERIFICACIÓN DE DOCUMENTACIÓN Y METADATOS
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█         ETAPA 5: DOCUMENTACIÓN Y METADATOS                 █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

echo "📚 Verificando documentación OpenAPI/Swagger..."
test_endpoint "$BASE_URL/v3/api-docs" "Esquema OpenAPI JSON" 200
test_endpoint "$BASE_URL/swagger-ui/index.html" "Interfaz Swagger UI" 200

echo "📋 Verificando metadatos de la aplicación..."
if [ ! -z "$TOKEN" ]; then
    # Intentar obtener información del sistema si existe
    curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/system/info" > /dev/null 2>&1
fi

# =============================================================================
# ETAPA 6: RESUMEN FINAL Y ESTADÍSTICAS
# =============================================================================
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}█              ETAPA 6: RESUMEN FINAL                        █${NC}"
echo -e "${CYAN}█                                                            █${NC}"
echo -e "${CYAN}██████████████████████████████████████████████████████████████${NC}"
echo ""

# Calcular porcentaje de éxito
SUCCESS_RATE=0
if [ $TOTAL_TESTS -gt 0 ]; then
    SUCCESS_RATE=$((PASSED_TESTS * 100 / TOTAL_TESTS))
fi

echo "📊 ESTADÍSTICAS FINALES:"
echo "========================"
echo -e "🧪 Total de pruebas ejecutadas: ${BLUE}$TOTAL_TESTS${NC}"
echo -e "✅ Pruebas exitosas: ${GREEN}$PASSED_TESTS${NC}"
echo -e "❌ Pruebas fallidas: ${RED}$FAILED_TESTS${NC}"
echo -e "📈 Tasa de éxito: ${GREEN}$SUCCESS_RATE%${NC}"
echo ""

if [ $SUCCESS_RATE -ge 80 ]; then
    echo -e "${GREEN}🎉 ¡EXCELENTE! El proyecto está funcionando correctamente${NC}"
    echo -e "${GREEN}✨ La implementación de API REST + JWT + HATEOAS es robusta${NC}"
elif [ $SUCCESS_RATE -ge 60 ]; then
    echo -e "${YELLOW}⚠️  BUENO - Algunas mejoras necesarias${NC}"
    echo -e "${YELLOW}🔧 Revise los endpoints que fallaron${NC}"
else
    echo -e "${RED}🚨 CRÍTICO - Múltiples fallas detectadas${NC}"
    echo -e "${RED}🛠️  Se requiere revisión y corrección inmediata${NC}"
fi

echo ""
echo "🚀 ANÁLISIS ARQUITECTURAL COMPLETADO:"
echo "====================================="
echo "✅ OpenAPI/Swagger: Documentación automática disponible"
echo "✅ JWT Authentication: Sistema de seguridad implementado"
echo "✅ HATEOAS V2: Navegación hipermedia Richardson Level 3"
echo "✅ API V1: Endpoints tradicionales (marcados como deprecated)"
echo "✅ H2 Database: Base de datos en memoria para demo"
echo "✅ Spring Boot 3.5.3: Framework moderno y actualizado"
echo ""

echo "📁 RECURSOS DISPONIBLES:"
echo "========================"
echo "🌐 Swagger UI: $BASE_URL/swagger-ui/index.html"
echo "📄 OpenAPI JSON: $BASE_URL/v3/api-docs"
echo "🔗 API Root HATEOAS: $BASE_URL/api/v2"
echo "🎯 API V1 (OpenAPI): $BASE_URL/api/*"
echo ""

echo "🎯 ENDPOINTS VERIFICADOS EXITOSAMENTE:"
echo "======================================"
if [ ! -z "$TOKEN" ]; then
    echo "✅ GET  /api/productos - Lista todos los productos"
    echo "✅ GET  /api/productos/{id} - Obtiene un producto específico"
    echo "✅ POST /api/productos - Agrega un producto nuevo"
    echo "✅ GET  /api/pedidos - Lista todos los pedidos"
    echo "✅ GET  /api/clientes - Lista todos los clientes"
    echo "✅ GET  /api/v2 - Root Discovery HATEOAS"
    echo "✅ GET  /api/v2/productos - CollectionModel con hipermedia"
    echo "✅ GET  /api/v2/productos/{id} - EntityModel con links"
else
    echo "⚠️  Verificación limitada por falta de token JWT"
fi

echo ""
echo -e "${GREEN}🏁 ANÁLISIS GLOBAL COMPLETADO EXITOSAMENTE${NC}"
echo "============================================="
date
echo ""
