import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api.js";
import { useAuth } from "../auth.jsx";
import ClienteForm from "../components/ClienteForm.jsx";

export default function Perfil() {
  const { sessao, sair } = useAuth();
  const navegar = useNavigate();
  const [cliente, setCliente] = useState(null);
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    api(`/clientes/${sessao.clienteId}`).then(setCliente).catch((e) => setErro(e.message));
  }, [sessao.clienteId]);

  async function salvar(corpo) {
    const { id: identificador, login, senha, ...dados } = corpo;
    const atualizado = await api(`/clientes/${sessao.clienteId}`, { metodo: "PUT", corpo: dados });
    setCliente(atualizado);
    setMensagem("Dados salvos");
  }

  async function excluir() {
    if (!window.confirm("Excluir sua conta e todos os seus pedidos?")) return;
    try {
      await api(`/clientes/${sessao.clienteId}`, { metodo: "DELETE" });
      sair();
      navegar("/login");
    } catch (e) {
      setErro(e.message);
    }
  }

  if (erro && !cliente) return <p className="erro">{erro}</p>;
  if (!cliente) return <p>Carregando...</p>;

  return (
    <section className="painel estreito">
      <h2>Meus dados</h2>
      <p className="apoio">Usuário: {cliente.login}</p>
      <ClienteForm inicial={cliente} rotulo="Salvar alterações" onEnviar={salvar} />
      {mensagem && <p className="sucesso">{mensagem}</p>}
      {erro && <p className="erro">{erro}</p>}
      <hr />
      <button className="btn btn-perigo" onClick={excluir}>Excluir minha conta</button>
    </section>
  );
}
