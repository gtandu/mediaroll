import { Component, OnInit } from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
import { AuthentificationService } from '../../services/authentification.service';

@Component({
  selector: 'app-medialist',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.css']
})
export class MediaListComponent implements OnInit {
  medias: Media[] = [];

  constructor(private mediaService: MediaService) { }

  ngOnInit() {
    this.mediaService.getAllMedias().subscribe(medias => {
      this.medias = medias;
    });

  }

  showModal(el: HTMLImageElement) {
    $('#myModal').show();
    $('#modalContent').attr('src', el.src);
    $('#modalContent').attr('data-id', el.getAttribute('data-id'));
    $('#caption').html($(this).attr('alt'));
    $('.navbar').css('z-index', '0');
    $('#slide-out').css('z-index', '0');

  }

  closeModal() {
    $('#myModal').hide();
    $('.navbar').css('z-index', '');
    $('#slide-out').css('z-index', '');
  }

  deleteMedia() {
    const mediaId = $('#modalContent').attr('data-id');
    const isDeleted = this.mediaService.deleteMediaById(mediaId).subscribe(
      response => {
        console.log(response);
      },
      err => {
        console.log('Error occured.');
      }
    );
  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }
}
