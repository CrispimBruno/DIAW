import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../api.js";

export default function PedidoForm() {
  const { id } = useParams();
  const navegar = useNavigate();
  const [automoveis, setAutomoveis] = useState([]);
  const [bancos, setBancos] = useState([]);
  const [dados, setDados] = useState({ automovelId: "", dataInicio: "", dataFim: "", bancoId: "", valorCredito: "" });
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    api("/automoveis").then(setAutomoveis).catch((e) => setErro(e.message));
    api("/bancos").then(setBancos).catch((e) => setErro(e.message));
    if (id) {
      api(`/pedidos/${id}`)
        .then((p) =>
          setDados({
            automovelId: String(p.automovel.id),
            dataInicio: p.dataInicio,
            dataFim: p.dataFim,
            bancoId: "",
            valorCredito: ""
          })
        )
        .catch((e) => setErro(e.message));
    }
  }, [id]);

  function alterar(campo, valor) {
    setDados((atual) => ({ ...atual, [campo]: valor }));
  }

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");
    setEnviando(true);
    const comCredito = dados.bancoId && dados.valorCredito;
    const corpo = {
      automovelId: Number(dados.automovelId),
      dataInicio: dados.dataInicio,
      dataFim: dados.dataFim,
      bancoId: comCredito ? Number(dados.bancoId) : null,
      valorCredito: comCredito ? Number(dados.valorCredito) : null
    };
    try {
      await api(id ? `/pedidos/${id}` : "/pedidos", { metodo: id ? "PUT" : "POST", corpo });
      navegar("/");
    } catch (e) {
      setErro(e.message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <section className="painel estreito">
      <h2>{id ? "Modificar pedido" : "Novo pedido de aluguel"}</h2>
      <form className="formulario" onSubmit={enviar}>
        <label>Automóvel
          <select value={dados.automovelId} onChange={(e) => alterar("automovelId", e.target.value)} required>
            <option value="">Selecione</option>
            {automoveis.map((a) => (
              <option key={a.id} value={a.id}>{a.marca} {a.modelo} {a.ano} — {a.placa}</option>
            ))}
          </select>
        </label>
        <div className="grade">
          <label>Retirada
            <input type="date" value={dados.dataInicio} onChange={(e) => alterar("dataInicio", e.target.value)} required />
          </label>
          <label>Devolução
            <input type="date" value={dados.dataFim} onChange={(e) => alterar("dataFim", e.target.value)} required />
          </label>
        </div>
        <fieldset>
          <legend>Contrato de crédito (opcional)</legend>
          <div className="grade">
            <label>Banco
              <select value={dados.bancoId} onChange={(e) => alterar("bancoId", e.target.value)}>
                <option value="">Sem crédito</option>
                {bancos.map((b) => <option key={b.id} value={b.id}>{b.nome}</option>)}
              </select>
            </label>
            <label>Valor financiado (R$)
              <input type="number" min="0.01" step="0.01" value={dados.valorCredito} onChange={(e) => alterar("valorCredito", e.target.value)} />
            </label>
          </div>
        </fieldset>
        {erro && <p className="erro">{erro}</p>}
        <button className="btn" disabled={enviando}>{id ? "Salvar pedido" : "Enviar pedido"}</button>
      </form>
    </section>
  );
}
