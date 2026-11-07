import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api.js";
import { useAuth } from "../auth.jsx";
import Status from "../components/Status.jsx";
import { formatarData, formatarMoeda } from "../util.js";

function Avaliacao({ pedido, onConcluir, onErro }) {
  const [parecer, setParecer] = useState(pedido.parecer ?? "");

  async function enviar(status) {
    try {
      await api(`/pedidos/${pedido.id}/avaliacao`, { metodo: "PUT", corpo: { status, parecer } });
      onConcluir();
    } catch (e) {
      onErro(e.message);
    }
  }

  const analise = pedido.status === "EM_ANALISE";
  const aprovado = pedido.status === "APROVADO";
  if (!analise && !aprovado) return null;

  return (
    <div className="avaliacao">
      <input placeholder="Parecer" value={parecer} onChange={(e) => setParecer(e.target.value)} />
      {analise && (
        <>
          <button className="btn" onClick={() => enviar("APROVADO")}>Aprovar</button>
          <button className="btn btn-perigo" onClick={() => enviar("REPROVADO")}>Reprovar</button>
        </>
      )}
      {aprovado && <button className="btn" onClick={() => enviar("CONTRATADO")}>Executar contrato</button>}
    </div>
  );
}

export default function Pedidos() {
  const { ehAgente } = useAuth();
  const [pedidos, setPedidos] = useState(null);
  const [erro, setErro] = useState("");

  function carregar() {
    api("/pedidos").then(setPedidos).catch((e) => setErro(e.message));
  }

  useEffect(carregar, []);

  async function cancelar(id) {
    if (!window.confirm("Cancelar este pedido?")) return;
    try {
      await api(`/pedidos/${id}`, { metodo: "DELETE" });
      carregar();
    } catch (e) {
      setErro(e.message);
    }
  }

  return (
    <section className="painel">
      <div className="cabecalho-painel">
        <h2>{ehAgente ? "Pedidos recebidos" : "Meus pedidos"}</h2>
        {!ehAgente && <Link className="btn" to="/pedidos/novo">Novo pedido</Link>}
      </div>
      {erro && <p className="erro">{erro}</p>}
      {pedidos === null && !erro && <p>Carregando...</p>}
      {pedidos?.length === 0 && (
        <p className="vazio">
          {ehAgente ? "Nenhum pedido aguardando análise." : "Você ainda não tem pedidos. Crie o primeiro para acompanhar o status aqui."}
        </p>
      )}
      <ul className="pedidos">
        {pedidos?.map((p) => (
          <li key={p.id} className="pedido">
            <div className="pedido-topo">
              <strong>{p.automovel.marca} {p.automovel.modelo} {p.automovel.ano}</strong>
              <Status valor={p.status} />
            </div>
            <p className="apoio">
              Placa {p.automovel.placa} · {formatarData(p.dataInicio)} a {formatarData(p.dataFim)}
              {ehAgente && ` · Cliente: ${p.clienteNome}`}
            </p>
            {p.bancoNome && <p className="apoio">Crédito de {formatarMoeda(p.valorCredito)} no {p.bancoNome}</p>}
            {p.parecer && <p className="parecer">Parecer: {p.parecer}</p>}
            {!ehAgente && p.status === "EM_ANALISE" && (
              <div className="acoes">
                <Link className="btn btn-claro" to={`/pedidos/${p.id}/editar`}>Modificar</Link>
                <button className="btn btn-perigo" onClick={() => cancelar(p.id)}>Cancelar</button>
              </div>
            )}
            {!ehAgente && p.status === "APROVADO" && (
              <div className="acoes">
                <button className="btn btn-perigo" onClick={() => cancelar(p.id)}>Cancelar</button>
              </div>
            )}
            {ehAgente && <Avaliacao pedido={p} onConcluir={carregar} onErro={setErro} />}
          </li>
        ))}
      </ul>
    </section>
  );
}
