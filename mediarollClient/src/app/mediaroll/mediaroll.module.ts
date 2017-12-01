import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

import { HomeComponent } from "./home/home.component";
import { MediarollRoutingModule } from "./mediaroll-routing.module";
import { AuthGuard } from '../guards/auth-guard.service';

import { MediaService } from "../services/media/media.service";
import { FileUploadModule } from 'ng2-file-upload';



@NgModule({
  imports: [
    CommonModule,
    MediarollRoutingModule,
    FileUploadModule
  ],
  declarations: [
    HomeComponent
  ],
  providers: [AuthGuard, MediaService]
})
export class MediarollModule {}