import { Link, useNavigate } from "react-router-dom";
import { api } from "../api.js";
import ClienteForm from "../components/ClienteForm.jsx";

export default function Cadastro() {
  const navegar = useNavigate();

  async function cadastrar(corpo) {
    await api("/clientes", { metodo: "POST", corpo });
    navegar("/login");
  }

  return (
    <div className="entrada entrada-larga">
      <div className="entrada-faixa">
        <h1>Pista</h1>
        <p>Cadastre seus dados para solicitar o aluguel de um automóvel.</p>
      </div>
      <div className="entrada-form">
        <h2>Criar conta</h2>
        <ClienteForm criar rotulo="Criar conta" onEnviar={cadastrar} />
        <p className="rodape-form">Já tem conta? <Link to="/login">Entrar</Link></p>
      </div>
    </div>
  );
}
