import { useEffect, useState } from "react";
import { api } from "../api.js";
import { formatarMoeda } from "../util.js";

export default function Clientes() {
  const [clientes, setClientes] = useState([]);
  const [erro, setErro] = useState("");

  function carregar() {
    api("/clientes").then(setClientes).catch((e) => setErro(e.message));
  }

  useEffect(carregar, []);

  async function excluir(id) {
    if (!window.confirm("Excluir este cliente e seus pedidos?")) return;
    try {
      await api(`/clientes/${id}`, { metodo: "DELETE" });
      carregar();
    } catch (e) {
      setErro(e.message);
    }
  }

  return (
    <section className="painel">
      <h2>Clientes</h2>
      {erro && <p className="erro">{erro}</p>}
      {clientes.length === 0 ? (
        <p className="vazio">Nenhum cliente cadastrado ainda.</p>
      ) : (
        <div className="rolagem">
          <table>
            <thead>
              <tr><th>Nome</th><th>CPF</th><th>RG</th><th>Profissão</th><th>Rendimentos</th><th></th></tr>
            </thead>
            <tbody>
              {clientes.map((c) => (
                <tr key={c.id}>
                  <td>{c.nome}</td>
                  <td>{c.cpf}</td>
                  <td>{c.rg}</td>
                  <td>{c.profissao}</td>
                  <td>{c.rendimentos.map((r) => `${r.entidadeEmpregadora}: ${formatarMoeda(r.valor)}`).join(" / ") || "—"}</td>
                  <td><button className="btn btn-perigo" onClick={() => excluir(c.id)}>Excluir</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
