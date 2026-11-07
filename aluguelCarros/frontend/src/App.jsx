import { Navigate, Route, Routes } from "react-router-dom";
import { useAuth } from "./auth.jsx";
import Layout from "./components/Layout.jsx";
import Login from "./pages/Login.jsx";
import Cadastro from "./pages/Cadastro.jsx";
import Pedidos from "./pages/Pedidos.jsx";
import PedidoForm from "./pages/PedidoForm.jsx";
import Perfil from "./pages/Perfil.jsx";
import Clientes from "./pages/Clientes.jsx";

function Protegida({ somente, children }) {
  const { sessao, ehAgente } = useAuth();
  if (!sessao) return <Navigate to="/login" replace />;
  if (somente === "cliente" && ehAgente) return <Navigate to="/" replace />;
  if (somente === "agente" && !ehAgente) return <Navigate to="/" replace />;
  return children;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route element={<Layout />}>
        <Route path="/" element={<Protegida><Pedidos /></Protegida>} />
        <Route path="/pedidos/novo" element={<Protegida somente="cliente"><PedidoForm /></Protegida>} />
        <Route path="/pedidos/:id/editar" element={<Protegida somente="cliente"><PedidoForm /></Protegida>} />
        <Route path="/perfil" element={<Protegida somente="cliente"><Perfil /></Protegida>} />
        <Route path="/clientes" element={<Protegida somente="agente"><Clientes /></Protegida>} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
