-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-11-2026 a las 23:44:01
-- Versión del servidor: 10.11.16-MariaDB
-- Versión de PHP: 8.3.36

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `demo`
--
CREATE DATABASE IF NOT EXISTS `demo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `demo`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carritos`
--

CREATE TABLE `carritos` (
  `id_carrito` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `carritos`
--

INSERT INTO `carritos` (`id_carrito`, `cliente_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` bigint(20) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `apellido`, `email`, `nombre`) VALUES
(1, 'Pérez', 'ana.prueba@example.com', 'Ana'),
(2, 'Díaz', 'bruno.prueba@example.com', 'Bruno'),
(3, 'López', 'carla.prueba@example.com', 'Carla'),
(4, 'Ruiz', 'diego.prueba@example.com', 'Diego'),
(5, 'Molina', 'eva.prueba@example.com', 'Eva');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_carrito`
--

CREATE TABLE `detalles_carrito` (
  `id_detalle_carrito` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(12,2) NOT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  `carrito_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalles_carrito`
--

INSERT INTO `detalles_carrito` (`id_detalle_carrito`, `cantidad`, `precio_unitario`, `subtotal`, `carrito_id`, `producto_id`) VALUES
(3, 1, 42000.00, 42000.00, 3, 3),
(4, 1, 22000.00, 22000.00, 4, 4),
(5, 1, 27000.00, 27000.00, 5, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_pedido`
--

CREATE TABLE `detalles_pedido` (
  `id_detalle_pedido` bigint(20) NOT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `precio_unitario` double DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `pedido_id` bigint(20) DEFAULT NULL,
  `producto_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalles_pedido`
--

INSERT INTO `detalles_pedido` (`id_detalle_pedido`, `cantidad`, `precio_unitario`, `subtotal`, `pedido_id`, `producto_id`) VALUES
(1, 1, 12500, 12500, 1, 1),
(2, 1, 58000, 58000, 1, 2),
(3, 1, 58000, 58000, 2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evidencias`
--

CREATE TABLE `evidencias` (
  `id` bigint(20) NOT NULL,
  `nombre_archivo` varchar(255) NOT NULL,
  `ruta_archivo` varchar(500) NOT NULL,
  `reclamo_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `evidencias`
--

INSERT INTO `evidencias` (`id`, `nombre_archivo`, `ruta_archivo`, `reclamo_id`) VALUES
(1, 'images (1).jpg', 'C:\\xampp\\htdocs\\AYDS 2\\G3_ProyectoFinal\\uploads\\evidencias\\9c08342c-7872-4157-adaf-7952b61f46cd.jpg', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` bigint(20) NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `monto_total` double DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `datos_entrega` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id_pedido`, `estado`, `fecha_creacion`, `monto_total`, `cliente_id`, `codigo`, `datos_entrega`) VALUES
(1, 'Cancelado', '2026-11-25 19:31:40.000000', 70500, 1, 'VG-1002', 'Ana Pérez, Av. Siempreviva 742, Buenos Aires. Teléfono: 11-5555-1234'),
(2, 'Pendiente', '2026-11-25 19:41:09.000000', 58000, 2, 'VG-1003', 'Soledad Maria, Av. Siempreviva 756, Buenos Aires. Teléfono: 11-4444-1234');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `stock_disponible` int(11) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio_unitario` decimal(38,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `stock_disponible`, `descripcion`, `precio_unitario`) VALUES
(1, 'Maceta reciclada', 20, 'Maceta de material recuperado', 12500.00),
(2, 'Compostera doméstica', 7, 'Compostera para el hogar', 58000.00),
(3, 'Jardinera vertical', 12, 'Jardinera de madera recuperada', 42000.00),
(4, 'Tabla de cocina', 15, 'Tabla artesanal de madera recuperada', 22000.00),
(5, 'Canasto tejido', 9, 'Canasto artesanal de fibras reutilizadas', 27000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reclamos`
--

CREATE TABLE `reclamos` (
  `id` bigint(20) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `motivo` varchar(120) NOT NULL,
  `descripcion` varchar(2000) NOT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `pedido_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `reclamos`
--

INSERT INTO `reclamos` (`id`, `tipo`, `motivo`, `descripcion`, `fecha_creacion`, `estado`, `cliente_id`, `pedido_id`) VALUES
(1, 'RECLAMO', 'Producto dañado', 'El producto llegó con una falla y no se puede utilizar.', '2026-11-25 19:41:14.000000', 'Pendiente', 2, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `carritos`
--
ALTER TABLE `carritos`
  ADD PRIMARY KEY (`id_carrito`),
  ADD UNIQUE KEY `UKf7xpwf4434eyfx1p8po60nqg2` (`cliente_id`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `UK1c96wv36rk2hwui7qhjks3mvg` (`email`);

--
-- Indices de la tabla `detalles_carrito`
--
ALTER TABLE `detalles_carrito`
  ADD PRIMARY KEY (`id_detalle_carrito`),
  ADD UNIQUE KEY `uk_detalle_carrito_producto` (`carrito_id`,`producto_id`),
  ADD KEY `FKhevjmyci4qj3vok0gvj2oo65c` (`producto_id`);

--
-- Indices de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD PRIMARY KEY (`id_detalle_pedido`),
  ADD KEY `FK4qmqlxyy78kjl4ec4wjnfmggu` (`pedido_id`),
  ADD KEY `FK8144uqs26ce7usdnqb1aml16` (`producto_id`);

--
-- Indices de la tabla `evidencias`
--
ALTER TABLE `evidencias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_evidencias_reclamo` (`reclamo_id`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD KEY `fk_pedidos_cliente_cu04` (`cliente_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`);

--
-- Indices de la tabla `reclamos`
--
ALTER TABLE `reclamos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_reclamos_cliente` (`cliente_id`),
  ADD KEY `idx_reclamos_pedido` (`pedido_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carritos`
--
ALTER TABLE `carritos`
  MODIFY `id_carrito` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `detalles_carrito`
--
ALTER TABLE `detalles_carrito`
  MODIFY `id_detalle_carrito` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  MODIFY `id_detalle_pedido` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `evidencias`
--
ALTER TABLE `evidencias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `reclamos`
--
ALTER TABLE `reclamos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `carritos`
--
ALTER TABLE `carritos`
  ADD CONSTRAINT `FKnxakr2a54l3yo8beiueoqr871` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id_cliente`);

--
-- Filtros para la tabla `detalles_carrito`
--
ALTER TABLE `detalles_carrito`
  ADD CONSTRAINT `FKguv56qo9p9d82s54esq5besrr` FOREIGN KEY (`carrito_id`) REFERENCES `carritos` (`id_carrito`),
  ADD CONSTRAINT `FKhevjmyci4qj3vok0gvj2oo65c` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id_producto`);

--
-- Filtros para la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD CONSTRAINT `FK4qmqlxyy78kjl4ec4wjnfmggu` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id_pedido`),
  ADD CONSTRAINT `FK8144uqs26ce7usdnqb1aml16` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id_producto`);

--
-- Filtros para la tabla `evidencias`
--
ALTER TABLE `evidencias`
  ADD CONSTRAINT `fk_evidencias_reclamo_cu04` FOREIGN KEY (`reclamo_id`) REFERENCES `reclamos` (`id`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_pedidos_cliente_cu04` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id_cliente`);

--
-- Filtros para la tabla `reclamos`
--
ALTER TABLE `reclamos`
  ADD CONSTRAINT `fk_reclamos_cliente_cu04` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id_cliente`),
  ADD CONSTRAINT `fk_reclamos_pedido_cu04` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id_pedido`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
