(function(){
  'use strict';

  const q=s=>document.querySelector(s);
  const clean=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  let areaCache=[];

  async function request(url,opt={}){
    const r=await fetch(url,{headers:{'Content-Type':'application/json'},...opt});
    let data=null;try{data=await r.json();}catch(_){}
    if(!r.ok)throw new Error(data?.erro||`Erro ${r.status}`);
    return data;
  }

  async function listar(){ return request('/api/areas'); }

  async function renderManager(){
    try{
      areaCache=await listar();
      q('#modalRoot').innerHTML=`<div class="modal-bg" onclick="if(event.target===this)closeModal()"><div class="modal">
        <div class="modal-head"><div><h2>Áreas e serviços</h2><div class="meta">Cadastre novas especialidades sem alterar o sistema.</div></div><button class="x" onclick="closeModal()">×</button></div>
        <div class="grid2"><div class="field"><label>Nova área / serviço</label><input id="areaNomeNovo" placeholder="Ex.: Nutrição"></div><div class="field"><label>Categoria</label><input id="areaCategoriaNova" placeholder="Ex.: Saúde, Estética, Medicina"></div></div>
        <button class="btn" style="margin-top:9px;background:var(--primary);color:#fff" onclick="frCriarArea()">Adicionar área</button>
        <div class="history" style="margin-top:16px">${areaCache.map(a=>`<div class="history-item"><div style="display:flex;justify-content:space-between;gap:8px;align-items:center"><div><b>${clean(a.nome)}</b><div>${clean(a.categoria||'Sem categoria')}</div></div><button class="btn ghost" onclick="frEditarArea(${Number(a.id)})">Editar</button></div></div>`).join('')||'<div class="meta">Nenhuma área cadastrada.</div>'}</div>
      </div></div>`;
    }catch(e){toast(e.message);}
  }

  window.openAreasManager=renderManager;

  window.frCriarArea=async function(){
    const nome=q('#areaNomeNovo')?.value.trim()||'',categoria=q('#areaCategoriaNova')?.value.trim()||'';
    if(!nome){toast('Informe o nome da área');return;}
    try{
      await request('/api/areas',{method:'POST',body:JSON.stringify({nome,categoria})});
      toast('Área adicionada');
      await load();
      await renderManager();
    }catch(e){toast(e.message);}
  };

  window.frEditarArea=async function(id){
    const atual=areaCache.find(a=>Number(a.id)===Number(id));
    if(!atual)return;
    const nome=prompt('Nome da área/serviço:',atual.nome||'');if(nome===null)return;
    const categoria=prompt('Categoria:',atual.categoria||'');if(categoria===null)return;
    try{
      await request(`/api/areas/${id}`,{method:'PUT',body:JSON.stringify({nome,categoria,ativo:true})});
      toast('Área atualizada');
      await load();
      await renderManager();
    }catch(e){toast(e.message);}
  };

  document.addEventListener('DOMContentLoaded',()=>q('#areasBtn')?.addEventListener('click',renderManager),{once:true});
})();
