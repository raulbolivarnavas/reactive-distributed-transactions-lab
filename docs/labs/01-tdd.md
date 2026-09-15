# Laboratorio 01: TDD del orquestador

1. Abre `CreateOrderSagaTest`: define el contrato de happy path y las listas de llamadas esperadas.
2. Para practicar RED, reemplaza temporalmente el cuerpo de `execute` por `Mono.error(new UnsupportedOperationException())`; ejecuta `gradle :order-service:test` y observa fallos.
3. Restaura la implementación y ejecuta las pruebas para GREEN.
4. Revisa las pruebas de Inventory, Payment y Shipping: solo se compensan pasos confirmados y en orden inverso.
5. Revisa refund fallido: la prueba exige intentar release y conservar COMPENSATION_FAILED.
6. Para REFACTOR extrae la lista de pasos hacia una definición de Saga sin modificar las expectativas; vuelve a ejecutar los tests.
7. Levanta Compose y comprueba los seis escenarios del README; inspecciona las bases de datos.

Las pruebas unitarias usan puertos en memoria para verificar el pipeline real del caso de uso. No requieren Docker. La validación de R2DBC y HTTP se realiza manualmente en este incremento; la automatización con Testcontainers vendrá después.
