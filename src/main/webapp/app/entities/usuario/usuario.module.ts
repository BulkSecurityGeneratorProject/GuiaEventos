import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TrabajoFinalSharedModule } from '../../shared';
import { TrabajoFinalAdminModule } from '../../admin/admin.module';
import {
    UsuarioService,
    UsuarioPopupService,
    UsuarioComponent,
    UsuarioDetailComponent,
    UsuarioDialogComponent,
    UsuarioPopupComponent,
    UsuarioDeletePopupComponent,
    UsuarioDeleteDialogComponent,
    usuarioRoute,
    usuarioPopupRoute,
    SerPrestadorComponent,
} from './';

const ENTITY_STATES = [
    ...usuarioRoute,
    ...usuarioPopupRoute,
];

@NgModule({
    imports: [
        TrabajoFinalSharedModule,
        TrabajoFinalAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UsuarioComponent,
        UsuarioDetailComponent,
        UsuarioDialogComponent,
        UsuarioDeleteDialogComponent,
        UsuarioPopupComponent,
        UsuarioDeletePopupComponent,
        SerPrestadorComponent,
    ],
    entryComponents: [
        UsuarioComponent,
        UsuarioDialogComponent,
        UsuarioPopupComponent,
        UsuarioDeleteDialogComponent,
        UsuarioDeletePopupComponent,
    ],
    providers: [
        UsuarioService,
        UsuarioPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TrabajoFinalUsuarioModule {}
