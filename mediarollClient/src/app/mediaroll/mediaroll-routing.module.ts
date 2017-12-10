import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from '../guards/auth-guard.service';


const mediarollRoutes: Routes = [
    {
        path: 'mediaroll',
        canActivate: [AuthGuard],
        children: [
            { path: 'home', component: HomeComponent }
        ]
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(mediarollRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class MediarollRoutingModule { }