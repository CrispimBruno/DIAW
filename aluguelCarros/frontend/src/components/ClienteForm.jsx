import { useState } from "react";

const VAZIO = { login: "", senha: "", rg: "", cpf: "", nome: "", endereco: "", profissao: "", rendimentos: [] };

export default function ClienteForm({ inicial, criar, rotulo, onEnviar }) {
  const [dados, setDados] = useState(inicial ?? VAZIO);
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  function alterar(campo, valor) {
    setDados((atual) => ({ ...atual, [campo]: valor }));
  }

  function alterarRendimento(indice, campo, valor) {
    const lista = dados.rendimentos.map((r, i) => (i === indice ? { ...r, [campo]: valor } : r));
    alterar("rendimentos", lista);
  }

  function adicionarRendimento() {
    alterar("rendimentos", [...dados.rendimentos, { entidadeEmpregadora: "", valor: "" }]);
  }

  function removerRendimento(indice) {
    alterar("rendimentos", dados.rendimentos.filter((_, i) => i !== indice));
  }

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");
    setEnviando(true);
    const corpo = {
      ...dados,
      rendimentos: dados.rendimentos.map((r) => ({ ...r, valor: Number(r.valor) }))
    };
    try {
      await onEnviar(corpo);
    } catch (e) {
      setErro(e.message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <form className="formulario" onSubmit={enviar}>
      {criar && (
        <div className="grade">
          <label>Usuário
            <input value={dados.login} onChange={(e) => alterar("login", e.target.value)} required minLength={3} />
          </label>
          <label>Senha
            <input type="password" value={dados.senha} onChange={(e) => alterar("senha", e.target.value)} required minLength={6} />
          </label>
        </div>
      )}
      <label>Nome completo
        <input value={dados.nome} onChange={(e) => alterar("nome", e.target.value)} required />
      </label>
      <div className="grade">
        <label>RG
          <input value={dados.rg} onChange={(e) => alterar("rg", e.target.value)} required />
        </label>
        <label>CPF (somente números)
          <input value={dados.cpf} onChange={(e) => alterar("cpf", e.target.value)} required pattern="\d{11}" maxLength={11} />
        </label>
      </div>
      <label>Endereço
        <input value={dados.endereco} onChange={(e) => alterar("endereco", e.target.value)} required />
      </label>
      <label>Profissão
        <input value={dados.profissao} onChange={(e) => alterar("profissao", e.target.value)} required />
      </label>

      <fieldset>
        <legend>Rendimentos (até 3)</legend>
        {dados.rendimentos.map((r, i) => (
          <div className="grade rendimento" key={i}>
            <label>Entidade empregadora
              <input value={r.entidadeEmpregadora} onChange={(e) => alterarRendimento(i, "entidadeEmpregadora", e.target.value)} required />
            </label>
            <label>Valor mensal (R$)
              <input type="number" min="0.01" step="0.01" value={r.valor} onChange={(e) => alterarRendimento(i, "valor", e.target.value)} required />
            </label>
            <button type="button" className="btn btn-perigo" onClick={() => removerRendimento(i)}>Remover</button>
          </div>
        ))}
        {dados.rendimentos.length < 3 && (
          <button type="button" className="btn btn-claro" onClick={adicionarRendimento}>Adicionar rendimento</button>
        )}
      </fieldset>

      {erro && <p className="erro">{erro}</p>}
      <button className="btn" disabled={enviando}>{enviando ? "Enviando..." : rotulo}</button>
    </form>
  );
}
