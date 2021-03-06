package com.edu.um.programacion2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.edu.um.programacion2.domain.Evento;
import com.edu.um.programacion2.domain.User;
import com.edu.um.programacion2.repository.EventoRepository;
import com.edu.um.programacion2.repository.search.EventoSearchRepository;
import com.edu.um.programacion2.security.SecurityUtils;
import com.edu.um.programacion2.service.dto.UserDTO;
import com.edu.um.programacion2.web.rest.errors.BadRequestAlertException;
import com.edu.um.programacion2.web.rest.errors.EmailAlreadyUsedException;
import com.edu.um.programacion2.web.rest.errors.InternalServerErrorException;
import com.edu.um.programacion2.web.rest.util.HeaderUtil;
import com.edu.um.programacion2.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Evento.
 */
@RestController
@RequestMapping("/api")
public class EventoResource {

    private final Logger log = LoggerFactory.getLogger(EventoResource.class);

    private static final String ENTITY_NAME = "evento";

    private final EventoRepository eventoRepository;

    private final EventoSearchRepository eventoSearchRepository;

    public EventoResource(EventoRepository eventoRepository, EventoSearchRepository eventoSearchRepository) {
        this.eventoRepository = eventoRepository;
        this.eventoSearchRepository = eventoSearchRepository;
    }

    /**
     * POST  /eventos : Create a new evento.
     *
     * @param evento the evento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evento, or with status 400 (Bad Request) if the evento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/eventos")
    @Timed
    public ResponseEntity<Evento> createEvento(@Valid @RequestBody Evento evento) throws URISyntaxException {
        log.debug("REST request to save Evento : {}", evento);
        if (evento.getId() != null) {
            throw new BadRequestAlertException("A new evento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evento result = eventoRepository.save(evento);
        eventoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eventos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /eventos : Updates an existing evento.
     *
     * @param evento the evento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evento,
     * or with status 400 (Bad Request) if the evento is not valid,
     * or with status 500 (Internal Server Error) if the evento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/eventos")
    @Timed
    public ResponseEntity<Evento> updateEvento(@Valid @RequestBody Evento evento) throws URISyntaxException {
        log.debug("REST request to update Evento : {}", evento);
        if (evento.getId() == null) {
            return createEvento(evento);
        }
        Evento result = eventoRepository.save(evento);
        eventoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, evento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eventos : get all the eventos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventos in body
     */
    @GetMapping("/eventos")
    @Timed
    public ResponseEntity<List<Evento>> getAllEventos(Pageable pageable) {
        log.debug("REST request to get a page of Eventos");
        List<Evento> evento = eventoRepository.findAllWithEagerRelationships();
        final Page<Evento> page = new PageImpl<>(evento);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /eventos/:id : get the "id" evento.
     *
     * @param id the id of the evento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evento, or with status 404 (Not Found)
     */
    @GetMapping("/eventos/{id}")
    @Timed
    public ResponseEntity<Evento> getEvento(@PathVariable Long id) {
        log.debug("REST request to get Evento : {}", id);
        Evento evento = eventoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(evento));
    }

    /**
     * DELETE  /eventos/:id : delete the "id" evento.
     *
     * @param id the id of the evento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/eventos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        log.debug("REST request to delete Evento : {}", id);
        eventoRepository.delete(id);
        eventoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/eventos?query=:query : search for the evento corresponding
     * to the query.
     *
     * @param query the query of the evento search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/eventos")
    @Timed
    public ResponseEntity<List<Evento>> searchEventos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Eventos for query {}", query);
        Page<Evento> page = eventoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/eventos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * POST  /eventos-usuario : Create a new evento.
     *
     * @param evento the evento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evento, or with status 400 (Bad Request) if the evento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/eventos-usuario")
    @Timed
    public ResponseEntity<Evento> createEventoUsuario(@Valid @RequestBody Evento evento) throws URISyntaxException {
        String login;
    	log.debug("REST request to save Evento : {}", evento);
        if (evento.getId() != null) {
            throw new BadRequestAlertException("A new evento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        login = SecurityUtils.getCurrentUserLogin().get();
        Evento result = eventoRepository.save(evento);
        eventoRepository.saveCreador(login ,result.getId());
        eventoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eventos-usuario/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /eventos-usuario : get all the eventos del usuario.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventos in body
     */
    @GetMapping("/eventos-usuario")
    @Timed
    public ResponseEntity<List<Evento>> getEventosUsuario(Pageable pageable) {
        log.debug("REST request to get a page of Eventos");
        //Page<Evento> page = eventoRepository.findAll(pageable);
        String login;
        Long id;
        login = SecurityUtils.getCurrentUserLogin().get();
        id = eventoRepository.getUserId(login);
        List<Evento> evento = eventoRepository.findUserEventos(id);
        final Page<Evento> page = new PageImpl<>(evento);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventos-usuario");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /eventos-usuario/estado/:id : Cambia el estado del evento :id
     *
     * @param id the id of the tags to add
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/eventos-usuario/estado/{id}")
    @Timed
    public String updateEstadoEvento(@PathVariable Long id) {
    	Evento evento = new Evento();
    	evento.setEstado(eventoRepository.getEstado(id));
        log.debug("REST request to update estado Evento: {}", id);
        evento.setEstado(!evento.isEstado());
        eventoRepository.updateEstado(id,evento.isEstado());
        return "/eventos-usuario";
    }
    
    /**
     * GET  /eventos-usario/registro/:id : registro en el evento id.
     *
     */
    @GetMapping("/eventos-usuario/registro/{idEvento}")
    @Timed
    public void registroEvento(@PathVariable Long idEvento) {
        String login;
        Long idUs;
        login = SecurityUtils.getCurrentUserLogin().get();
        idUs = eventoRepository.getUserId(login);
        log.debug("REST request add evento : {}", idEvento);
        eventoRepository.registroEventoSql(idEvento,idUs);
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, idTag.toString())).build();
    }
    
    /**
     * GET  /eventos-usario/favorito/:id : agregar a favoritos el evento id.
     *
     */
    @GetMapping("/eventos-usuario/favorito/{idEvento}")
    @Timed
    public void favoritoEvento(@PathVariable Long idEvento) {
        String login;
        Long idUs;
        login = SecurityUtils.getCurrentUserLogin().get();
        idUs = eventoRepository.getUserId(login);
        log.debug("REST request add evento : {}", idEvento);
        eventoRepository.favoritoEventoSql(idEvento,idUs);
        // return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, idTag.toString())).build();
    }
    
    /**
     * GET  /eventos/registrado : get all the eventos registrados del usuario.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventos in body
     */
    @GetMapping("/eventos/registrado")
    @Timed
    public ResponseEntity<List<Evento>> getEventosRegistrado(Pageable pageable) {
        log.debug("REST request to get a page of Eventos");
        String login;
        Long id;
        login = SecurityUtils.getCurrentUserLogin().get();
        id = eventoRepository.getUserId(login);
        List<Evento> evento = eventoRepository.findEventosRegistrado(id);
        final Page<Evento> page = new PageImpl<>(evento);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventos-usuario");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /eventos/favorito : get all the eventos favoritos del usuario.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventos in body
     */
    @GetMapping("/eventos/favorito")
    @Timed
    public ResponseEntity<List<Evento>> getEventosFavorito(Pageable pageable) {
        log.debug("REST request to get a page of Eventos");
        String login;
        Long id;
        login = SecurityUtils.getCurrentUserLogin().get();
        id = eventoRepository.getUserId(login);
        List<Evento> evento = eventoRepository.findEventosFavorito(id);
        final Page<Evento> page = new PageImpl<>(evento);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventos-usuario");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * DELETE  /eventos/favorito/:id : delete the "id" evento favorito.
     *
     * @param id the id of the evento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/eventos/favorito/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventoFavorito(@PathVariable Long id) {
        String login;
        Long idUs;
        login = SecurityUtils.getCurrentUserLogin().get();
        idUs = eventoRepository.getUserId(login);
        log.debug("REST request to delete Evento favorito : {}", id);
        eventoRepository.deleteFavorito(id, idUs);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
