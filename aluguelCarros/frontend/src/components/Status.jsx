import { STATUS } from "../util.js";

export default function Status({ valor }) {
  return <span className={`status status-${valor.toLowerCase()}`}>{STATUS[valor]}</span>;
}
