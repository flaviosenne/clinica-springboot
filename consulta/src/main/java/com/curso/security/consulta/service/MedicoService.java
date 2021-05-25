package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Medico;
import com.curso.security.consulta.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public Medico buscarPorUsuarioId(Long id){
        return medicoRepository.findByUsuarioId(id).orElse(new Medico());
    }

    @Transactional
    public void salvar(Medico medico) {
        medicoRepository.save(medico);
    }

    @Transactional
    public void editar(Medico medico) {
        // a variável está no estado persisiteten, por isso não precisa de usar o metodo save para atualizar
        Medico m2 = medicoRepository.findById(medico.getId()).get();
        m2.setCrm(medico.getCrm());
        m2.setDtInscricao(medico.getDtInscricao());
        m2.setNome(medico.getNome());
        if(!medico.getEspecialidades().isEmpty()){
            m2.getEspecialidades().addAll(medico.getEspecialidades());
        }
    }

    @Transactional
    public Medico buscarPorEmail(String email) {
        return medicoRepository.findByUsuarioEmail(email)
                .orElse(new Medico());
    }

    @Transactional
    public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
        Medico medico = medicoRepository.findById(idMed).get();

        medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));
    }

    @Transactional
    public List<Medico> buscarMedicosPorEspecialidades(String titulo) {
        return medicoRepository.findByMedicosPorEspecialidade(titulo);
    }
}
