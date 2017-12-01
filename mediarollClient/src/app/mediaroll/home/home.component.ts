import { Component, OnInit } from '@angular/core';
import { MediaService } from "../../services/media/media.service";
import { Media } from '../../models/media';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  medias: Media[] = [];

  constructor(private mediaService : MediaService) { }

  ngOnInit() {
   
    this.mediaService.getAllMedias().subscribe(medias => {
      console.log(medias);
      this.medias = medias;
  }); 

  }

  ngAfterViewChecked() {
    //$('.materialboxed').materialbox();
}

}
