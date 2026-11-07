import { useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { useAuth } from "../auth.jsx";

export default function Login() {
  const { sessao, entrar } = useAuth();
  const navegar = useNavigate();
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  if (sessao) return <Navigate to="/" replace />;

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");
    setEnviando(true);
    try {
      await entrar(login, senha);
      navegar("/");
    } catch (e) {
      setErro(e.message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="entrada">
      <div className="entrada-faixa">
        <h1>Pista</h1>
        <p>Peça, acompanhe e contrate o aluguel do seu carro em um só lugar.</p>
      </div>
      <form className="formulario entrada-form" onSubmit={enviar}>
        <h2>Entrar</h2>
        <label>Usuário
          <input value={login} onChange={(e) => setLogin(e.target.value)} required autoFocus />
        </label>
        <label>Senha
          <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} required />
        </label>
        {erro && <p className="erro">{erro}</p>}
        <button className="btn" disabled={enviando}>{enviando ? "Entrando..." : "Entrar"}</button>
        <p className="rodape-form">Ainda não tem conta? <Link to="/cadastro">Cadastre-se</Link></p>
      </form>
    </div>
  );
}
