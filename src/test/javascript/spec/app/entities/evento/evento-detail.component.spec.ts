/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { TrabajoFinalTestModule } from '../../../test.module';
import { EventoDetailComponent } from '../../../../../../main/webapp/app/entities/evento/evento-detail.component';
import { EventoService } from '../../../../../../main/webapp/app/entities/evento/evento.service';
import { Evento } from '../../../../../../main/webapp/app/entities/evento/evento.model';

describe('Component Tests', () => {

    describe('Evento Management Detail Component', () => {
        let comp: EventoDetailComponent;
        let fixture: ComponentFixture<EventoDetailComponent>;
        let service: EventoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TrabajoFinalTestModule],
                declarations: [EventoDetailComponent],
                providers: [
                    EventoService
                ]
            })
            .overrideTemplate(EventoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EventoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EventoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Evento(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.evento).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
