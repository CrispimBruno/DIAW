import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth.jsx";

export default function Layout() {
  const { sessao, ehAgente, sair } = useAuth();
  const navegar = useNavigate();

  function encerrar() {
    sair();
    navegar("/login");
  }

  return (
    <>
      <header className="topo">
        <span className="marca">Pista</span>
        {sessao && (
          <>
            <nav>
              <NavLink to="/" end>Pedidos</NavLink>
              {!ehAgente && <NavLink to="/pedidos/novo">Novo pedido</NavLink>}
              {!ehAgente && <NavLink to="/perfil">Meus dados</NavLink>}
              {ehAgente && <NavLink to="/clientes">Clientes</NavLink>}
            </nav>
            <div className="usuario">
              <span>{sessao.nome}</span>
              <button className="btn btn-claro" onClick={encerrar}>Sair</button>
            </div>
          </>
        )}
      </header>
      <main className="conteudo">
        <Outlet />
      </main>
    </>
  );
}
