import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

import { HomeComponent } from "./home/home.component";
import { MediarollRoutingModule } from "./mediaroll-routing.module";
import { AuthGuard } from '../guards/auth-guard.service';

@NgModule({
  imports: [
    CommonModule,
    MediarollRoutingModule
  ],
  declarations: [
    HomeComponent
  ],
  providers: [AuthGuard]
})
export class MediarollModule {}