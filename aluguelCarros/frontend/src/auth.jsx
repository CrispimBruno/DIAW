import { createContext, useContext, useState } from "react";
import { api, getSessao, limparSessao, salvarSessao } from "./api.js";

const Contexto = createContext(null);

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(getSessao);

  async function entrar(login, senha) {
    const dados = await api("/auth/login", { metodo: "POST", corpo: { login, senha } });
    salvarSessao(dados);
    setSessao(dados);
  }

  function sair() {
    limparSessao();
    setSessao(null);
  }

  const ehAgente = sessao ? sessao.perfil !== "CLIENTE" : false;

  return <Contexto.Provider value={{ sessao, ehAgente, entrar, sair }}>{children}</Contexto.Provider>;
}

export const useAuth = () => useContext(Contexto);
