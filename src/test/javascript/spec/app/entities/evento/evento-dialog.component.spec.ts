/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { TrabajoFinalTestModule } from '../../../test.module';
import { EventoDialogComponent } from '../../../../../../main/webapp/app/entities/evento/evento-dialog.component';
import { EventoService } from '../../../../../../main/webapp/app/entities/evento/evento.service';
import { Evento } from '../../../../../../main/webapp/app/entities/evento/evento.model';
import { CategoriaService } from '../../../../../../main/webapp/app/entities/categoria';
import { UsuarioService } from '../../../../../../main/webapp/app/entities/usuario';
import { TagsService } from '../../../../../../main/webapp/app/entities/tags';

describe('Component Tests', () => {

    describe('Evento Management Dialog Component', () => {
        let comp: EventoDialogComponent;
        let fixture: ComponentFixture<EventoDialogComponent>;
        let service: EventoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TrabajoFinalTestModule],
                declarations: [EventoDialogComponent],
                providers: [
                    CategoriaService,
                    UsuarioService,
                    TagsService,
                    EventoService
                ]
            })
            .overrideTemplate(EventoDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EventoDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EventoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Evento(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.evento = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'eventoListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Evento();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.evento = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'eventoListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
